package com.wartime.system.security;

import com.wartime.system.exception.EncryptionException;
import com.wartime.system.exception.DecryptionException;

public class ReverseCipherStrategy implements EncryptionStrategy {

    @Override
    public String encrypt(String data, String key) {
        if (data == null) {
            throw new EncryptionException("Data cannot be null");
        }
        return new StringBuilder(data).reverse().toString();
    }

    @Override
    public String decrypt(String data, String key) {
        if (data == null) {
            throw new DecryptionException("Data cannot be null");
        }
        return new StringBuilder(data).reverse().toString();
    }

    @Override
    public String getName() {
        return "Reverse Cipher";
    }
}
