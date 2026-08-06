package com.wartime.system.security;

public class CaesarCipherStrategy implements EncryptionStrategy {

    @Override
    public String encrypt(String data, String key) {
        int shift = key.length() % 26;
        StringBuilder result = new StringBuilder();
        for (char character : data.toCharArray()) {
            if (Character.isLowerCase(character)) {
                int originalAlphabetPosition = character - 'a';
                int newAlphabetPosition = (originalAlphabetPosition + shift) % 26;
                char newCharacter = (char) ('a' + newAlphabetPosition);
                result.append(newCharacter);
            } else if (Character.isUpperCase(character)) {
                int originalAlphabetPosition = character - 'A';
                int newAlphabetPosition = (originalAlphabetPosition + shift) % 26;
                char newCharacter = (char) ('A' + newAlphabetPosition);
                result.append(newCharacter);
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }

    @Override
    public String decrypt(String data, String key) {
        int shift = key.length() % 26;
        StringBuilder result = new StringBuilder();
        for (char character : data.toCharArray()) {
            if (Character.isLowerCase(character)) {
                int originalAlphabetPosition = character - 'a';
                int newAlphabetPosition = (originalAlphabetPosition - shift) % 26;
                if (newAlphabetPosition < 0) {
                    newAlphabetPosition += 26;
                }
                char newCharacter = (char) ('a' + newAlphabetPosition);
                result.append(newCharacter);
            } else if (Character.isUpperCase(character)) {
                int originalAlphabetPosition = character - 'A';
                int newAlphabetPosition = (originalAlphabetPosition - shift) % 26;
                if (newAlphabetPosition < 0) {
                    newAlphabetPosition += 26;
                }
                char newCharacter = (char) ('A' + newAlphabetPosition);
                result.append(newCharacter);
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }

    @Override
    public String getName() {
        return "Caesar Cipher";
    }
}
