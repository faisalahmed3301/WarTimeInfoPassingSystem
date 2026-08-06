package com.wartime.system.model;

public class SecureMessage extends AbstractMessage {
    private String encryptedContent;
    private String encryptionKey;
    private String encryptionStrategyName;
    private Rank targetRank;
    private boolean isRead;
    private boolean emergency;

    private SecureMessage(Builder builder) {
        super(builder.encryptedContent, builder.senderRank, builder.targetGroup, builder.targetUser);
        this.encryptedContent = builder.encryptedContent;
        this.encryptionKey = builder.encryptionKey;
        this.encryptionStrategyName = builder.encryptionStrategyName;
        this.targetRank = builder.targetRank;
        this.isRead = builder.isRead;
        this.emergency = builder.emergency;
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

    public boolean isEmergency() {
        return emergency;
    }

    public void setEmergency(boolean emergency) {
        this.emergency = emergency;
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
        private boolean emergency = false;

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

        public Builder setEmergency(boolean emergency) {
            this.emergency = emergency;
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
