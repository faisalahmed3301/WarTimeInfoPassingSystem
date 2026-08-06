package com.wartime.system.security;

import com.wartime.system.exception.EncryptionException;
import com.wartime.system.exception.DecryptionException;

public class XORCipherStrategy implements EncryptionStrategy {

    @Override
    public String encrypt(String data, String key) {
        if (data == null) {
            throw new EncryptionException("Data cannot be null");
        }
        if (key == null || key.isEmpty()) {
            throw new EncryptionException("Encryption key cannot be null or empty for XOR Cipher");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            sb.append((char) (data.charAt(i) ^ key.charAt(i % key.length())));
        }
        return sb.toString();
    }

    @Override
    public String decrypt(String data, String key) {
        if (data == null) {
            throw new DecryptionException("Data cannot be null");
        }
        if (key == null || key.isEmpty()) {
            throw new DecryptionException("Decryption key cannot be null or empty for XOR Cipher");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            sb.append((char) (data.charAt(i) ^ key.charAt(i % key.length())));
        }
        return sb.toString();
    }

    @Override
    public String getName() {
        return "XOR Cipher";
    }
}
