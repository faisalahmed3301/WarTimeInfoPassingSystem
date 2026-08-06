package com.wartime.system.service;

import com.wartime.system.model.AbstractUser;
import com.wartime.system.model.SecureMessage;
import com.wartime.system.security.EncryptionContext;
import com.wartime.system.security.EncryptionStrategy;
import com.wartime.system.security.SecurityContext;
import com.wartime.system.exception.WarTimeException;

import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;

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

    public void sendMessage(String plainText, String key, String strategyName, String appointment) {
      
        AbstractUser currentUser = SecurityContext.getInstance().getCurrentUser();
        if (currentUser == null)
            throw new WarTimeException("No user logged in");

        // Find strategy
        EncryptionStrategy strategy = resolveStrategy(strategyName);
        encryptionContext.setStrategy(strategy);

        // Parse appointment to Rank
        com.wartime.system.model.Rank targetRank;
        try {
            targetRank = com.wartime.system.model.Rank.valueOf(appointment.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new WarTimeException("Invalid Appointment Rank: " + appointment);
        }

        String encryptedText = encryptionContext.encrypt(plainText, key);

        SecureMessage message = new SecureMessage.Builder()
                .setEncryptedContent(encryptedText)
                .setEncryptionKey(key)
                .setEncryptionStrategyName(strategyName)
                .setSenderRank(currentUser.getRank())
                .setTargetRank(targetRank)
                .build();

        messages.add(message);
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
    }
}
