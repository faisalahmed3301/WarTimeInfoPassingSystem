package wartimeinfosystem.encryption;

import java.util.HashMap;
import java.util.Map;

/**
 * Substitution Cipher implementation
 */
public class SubstitutionCipher extends EncryptionAlgorithm {
    
    public SubstitutionCipher() {
        super("Substitution Cipher");
    }
    
    @Override
    public String encrypt(String message, String key) {
        if (!validateKey(key)) {
            return message;
        }
        
        Map<Character, Character> substitutionMap = generateSubstitutionMap(key, true);
        StringBuilder encrypted = new StringBuilder();
        
        for (char c : message.toCharArray()) {
            encrypted.append(substitutionMap.getOrDefault(c, c));
        }
        
        return encrypted.toString();
    }
    
    @Override
    public String decrypt(String encryptedMessage, String key) {
        if (!validateKey(key)) {
            return encryptedMessage;
        }
        
        Map<Character, Character> substitutionMap = generateSubstitutionMap(key, false);
        StringBuilder decrypted = new StringBuilder();
        
        for (char c : encryptedMessage.toCharArray()) {
            decrypted.append(substitutionMap.getOrDefault(c, c));
        }
        
        return decrypted.toString();
    }
    
    private Map<Character, Character> generateSubstitutionMap(String key, boolean forEncryption) {
        Map<Character, Character> map = new HashMap<>();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String shuffled = shuffleAlphabet(alphabet, key);
        
        for (int i = 0; i < alphabet.length(); i++) {
            if (forEncryption) {
                map.put(alphabet.charAt(i), shuffled.charAt(i));
            } else {
                map.put(shuffled.charAt(i), alphabet.charAt(i));
            }
        }
        
        return map;
    }
    
    private String shuffleAlphabet(String alphabet, String key) {
        int seed = key.hashCode();
        char[] chars = alphabet.toCharArray();
        
        for (int i = chars.length - 1; i > 0; i--) {
            seed = (seed * 1103515245 + 12345) & 0x7fffffff;
            int j = seed % (i + 1);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
        
        return new String(chars);
    }
}
