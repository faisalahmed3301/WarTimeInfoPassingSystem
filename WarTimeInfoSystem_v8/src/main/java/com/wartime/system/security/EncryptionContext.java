package com.wartime.system.security;

public class EncryptionContext {
    private EncryptionStrategy strategy;

    public void setStrategy(EncryptionStrategy strategy) {
        this.strategy = strategy;
    }

    public String encrypt(String data, String key) {
        if (strategy == null) {
            throw new IllegalStateException("Encryption strategy not set");
        }
        return strategy.encrypt(data, key);
    }

    public String decrypt(String data, String key) {
        if (strategy == null) {
            throw new IllegalStateException("Encryption strategy not set");
        }
        return strategy.decrypt(data, key);
    }
}
