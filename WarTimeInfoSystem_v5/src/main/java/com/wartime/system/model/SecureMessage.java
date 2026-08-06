package com.wartime.system.model;

public class SecureMessage extends AbstractMessage {
    private String encryptedContent;
    // We store the key hash or the actual key? "Receiver enters decryption key.
    // Only correct key reveals".
    // We need to verify if the entered key applies to this message.
    // So we should store the encryption key (or hash) to validate against.
    // Requirement 2: "User enters message + encryption key".
    private String encryptionKey;
    private String encryptionStrategyName;
    private Rank targetRank;
    private boolean isRead;

    private SecureMessage(Builder builder) {
        super(builder.encryptedContent, builder.senderRank, builder.targetGroup, builder.targetUser);
        this.encryptedContent = builder.encryptedContent;
        this.encryptionKey = builder.encryptionKey;
        this.encryptionStrategyName = builder.encryptionStrategyName;
        this.targetRank = builder.targetRank;
        this.isRead = builder.isRead;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String decrypt(String inputKey, HelperDecryption func) {
        return null;
    }

    public String getEncryptedContent() {
        return encryptedContent;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public String getEncryptionStrategyName() {
        return encryptionStrategyName;
    }

    public Rank getTargetRank() {
        return targetRank;
    }

    public static class Builder {
        private String encryptedContent;
        private Rank senderRank;
        private String encryptionKey;
        private String encryptionStrategyName;
        private Rank targetRank;
        private Group targetGroup;
        private AbstractUser targetUser;
        private boolean isRead = false;

        public Builder setRead(boolean isRead) {
            this.isRead = isRead;
            return this;
        }

        public Builder setEncryptedContent(String encryptedContent) {
            this.encryptedContent = encryptedContent;
            return this;
        }

        public Builder setSenderRank(Rank senderRank) {
            this.senderRank = senderRank;
            return this;
        }

        public Builder setTargetRank(Rank targetRank) {
            this.targetRank = targetRank;
            return this;
        }

        public Builder setTargetGroup(Group targetGroup) {
            this.targetGroup = targetGroup;
            return this;
        }

        public Builder setTargetUser(AbstractUser targetUser) {
            this.targetUser = targetUser;
            return this;
        }

        public Builder setEncryptionKey(String encryptionKey) {
            this.encryptionKey = encryptionKey;
            return this;
        }

        public Builder setEncryptionStrategyName(String encryptionStrategyName) {
            this.encryptionStrategyName = encryptionStrategyName;
            return this;
        }

        public SecureMessage build() {
            return new SecureMessage(this);
        }
    }

    // Functional interface helper for decryption callback if needed here,
    // or we just expose getters and let service handle logic.
    public interface HelperDecryption {
        String decrypt(String cipher, String key);
    }
}
