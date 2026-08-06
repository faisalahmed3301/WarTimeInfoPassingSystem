package com.wartime.system.security;

public class ReverseCipherStrategy implements EncryptionStrategy {

    @Override
    public String encrypt(String data, String key) {
        return new StringBuilder(data).reverse().toString();
    }

    @Override
    public String decrypt(String data, String key) {
        return new StringBuilder(data).reverse().toString();
    }

    @Override
    public String getName() {
        return "Reverse Cipher";
    }
}
