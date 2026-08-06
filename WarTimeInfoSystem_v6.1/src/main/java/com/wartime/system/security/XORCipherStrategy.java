package com.wartime.system.security;

public class XORCipherStrategy implements EncryptionStrategy {

    @Override
    public String encrypt(String data, String key) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            sb.append((char) (data.charAt(i) ^ key.charAt(i % key.length())));
        }
        return sb.toString();
    }

    @Override
    public String decrypt(String data, String key) {
        return encrypt(data, key); // XOR is symmetric
    }

    @Override
    public String getName() {
        return "XOR Cipher";
    }
}
