package com.wartime.system.service;

import com.wartime.system.model.AbstractUser;
import com.wartime.system.model.SecureMessage;
import com.wartime.system.security.EncryptionContext;
import com.wartime.system.security.EncryptionStrategy;
import com.wartime.system.security.SecurityContext;
import com.wartime.system.exception.WarTimeException;

import com.wartime.system.model.Group;
import com.wartime.system.model.Rank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageService {
    private static MessageService instance;
    private List<SecureMessage> messages = new ArrayList<>();
    private EncryptionContext encryptionContext = new EncryptionContext();

    private MessageService() {
    }

    public static synchronized MessageService getInstance() {
        if (instance == null) {
            instance = new MessageService();
        }
        return instance;
    }

    public void sendMessage(String plainText, String key, String strategyName, String appointment, Group group,
            AbstractUser targetUser) {

        AbstractUser currentUser = SecurityContext.getInstance().getCurrentUser();
        if (currentUser == null)
            throw new WarTimeException("No user logged in");

        // Find strategy
        EncryptionStrategy strategy = resolveStrategy(strategyName);
        encryptionContext.setStrategy(strategy);

        // Parse appointment to Rank
        com.wartime.system.model.Rank targetRank = null;
        if (appointment != null) {
            try {
                targetRank = com.wartime.system.model.Rank.valueOf(appointment.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new WarTimeException("Invalid Appointment Rank: " + appointment);
            }
        } else if (group == null && targetUser == null) {
            throw new WarTimeException("Either Appointment, Group, or Target User must be specified.");
        }

        String encryptedText = encryptionContext.encrypt(plainText, key);

        SecureMessage message = new SecureMessage.Builder()
                .setEncryptedContent(encryptedText)
                .setEncryptionKey(key)
                .setEncryptionStrategyName(strategyName)
                .setSenderRank(currentUser.getRank())
                .setTargetRank(targetRank)
                .setTargetGroup(group)
                .setTargetUser(targetUser)
                .build();

        messages.add(message);
        com.wartime.system.util.ExcelStorageManager.saveAllData();
    }

    public void sendEmergencyMessage(String plainText) {
        AbstractUser currentUser = SecurityContext.getInstance().getCurrentUser();
        if (currentUser == null)
            throw new WarTimeException("No user logged in");
        if (currentUser.getRank() != Rank.COMMANDER)
            throw new WarTimeException("Only Commanders can send emergency alerts");

        SecureMessage message = new SecureMessage.Builder()
                .setEncryptedContent(plainText)
                .setEncryptionKey("")
                .setEncryptionStrategyName("")
                .setSenderRank(currentUser.getRank())
                .setEmergency(true)
                .build();

        messages.add(message);
        com.wartime.system.util.ExcelStorageManager.saveAllData();
    }

    private EncryptionStrategy resolveStrategy(String name) {
        // Simple factory logic here or reflection
        // "Caesar Cipher", "Reverse Cipher", "XOR Cipher", "Base64"
        switch (name) {
            case "Caesar Cipher":
                return new com.wartime.system.security.CaesarCipherStrategy();
            case "Reverse Cipher":
                return new com.wartime.system.security.ReverseCipherStrategy();
            case "XOR Cipher":
                return new com.wartime.system.security.XORCipherStrategy();
            case "Base64 Encoding":
                return new com.wartime.system.security.Base64CipherStrategy();
            default:
                throw new WarTimeException("Unknown Strategy: " + name);
        }
    }

    public List<SecureMessage> getVisibleMessages(AbstractUser user) {
        return messages.stream()
                .filter(m -> {
                    // Emergency messages visible to everyone
                    if (m.isEmergency())
                        return true;

                    // Commander sees everything
                    if (user.getRank() == Rank.COMMANDER)
                        return true;

                    // If it's a private individual chat
                    if (m.getTargetUser() != null) {
                        return m.getTargetUser().getName().equals(user.getName());
                    }

                    // If it's a group message
                    if (m.getTargetGroup() != null) {
                        return m.getTargetGroup().isMember(user);
                    }

                    // If it's a rank-wide broadcast
                    if (m.getTargetRank() != null) {
                        return user.getRank().ordinal() <= m.getTargetRank().ordinal();
                    }

                    // Otherwise, use hierarchy logic
                    return user.canAccess(m.getSenderRank());
                })
                .collect(Collectors.toList());
    }

    public List<SecureMessage> getAllMessages() {
        return messages;
    }

    public String attemptDecryption(SecureMessage message, String inputKey) {
        if (!message.getEncryptionKey().equals(inputKey)) {
            throw new WarTimeException("Invalid Decryption Key");
        }

        EncryptionStrategy strategy = resolveStrategy(message.getEncryptionStrategyName());
        encryptionContext.setStrategy(strategy);
        return encryptionContext.decrypt(message.getEncryptedContent(), inputKey);
    }

    public void deleteMessage(SecureMessage message) {
        messages.remove(message);
        com.wartime.system.util.ExcelStorageManager.saveAllData();
    }

    public void deleteGroupMessages(Group group) {
        messages.removeIf(
                m -> m.getTargetGroup() != null && m.getTargetGroup().getName().equalsIgnoreCase(group.getName()));
        com.wartime.system.util.ExcelStorageManager.saveAllData();
    }

    public void markAsRead(SecureMessage message) {
        message.setRead(true);
        com.wartime.system.util.ExcelStorageManager.saveAllData();
    }
}
