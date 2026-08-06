package com.wartime.system.security;

import java.util.Base64;
import com.wartime.system.exception.EncryptionException;
import com.wartime.system.exception.DecryptionException;

public class Base64CipherStrategy implements EncryptionStrategy {

    @Override
    public String encrypt(String data, String key) {
        if (data == null) {
            throw new EncryptionException("Data cannot be null");
        }
        // Simple Base64, doesn't actually use key but satisfies interface
        return Base64.getEncoder().encodeToString(data.getBytes());
    }

    @Override
    public String decrypt(String data, String key) {
        if (data == null) {
            throw new DecryptionException("Data cannot be null");
        }
        try {
            return new String(Base64.getDecoder().decode(data));
        } catch (IllegalArgumentException e) {
            throw new DecryptionException("Invalid Base64 format during decryption", e);
        }
    }

    @Override
    public String getName() {
        return "Base64 Encoding";
    }
}
