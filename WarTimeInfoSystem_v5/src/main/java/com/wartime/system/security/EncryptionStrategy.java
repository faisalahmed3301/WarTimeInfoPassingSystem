package com.wartime.system.security;

public interface EncryptionStrategy {
    String encrypt(String data, String key);

    String decrypt(String data, String key);

    String getName();
}
