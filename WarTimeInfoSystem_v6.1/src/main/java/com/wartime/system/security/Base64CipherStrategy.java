package com.wartime.system.security;

import java.util.Base64;

public class Base64CipherStrategy implements EncryptionStrategy {

    @Override
    public String encrypt(String data, String key) {
        // Simple Base64, doesn't actually use key but satisfies interface
        return Base64.getEncoder().encodeToString(data.getBytes());
    }

    @Override
    public String decrypt(String data, String key) {
        return new String(Base64.getDecoder().decode(data));
    }

    @Override
    public String getName() {
        return "Base64 Encoding";
    }
}
