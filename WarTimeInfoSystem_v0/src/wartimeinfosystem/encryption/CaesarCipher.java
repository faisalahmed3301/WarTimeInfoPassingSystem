package wartimeinfosystem.encryption;

/**
 * Caesar Cipher implementation - inherits from EncryptionAlgorithm
 * Demonstrates inheritance and polymorphism
 */
public class CaesarCipher extends EncryptionAlgorithm {
    
    public CaesarCipher() {
        super("Caesar Cipher");
    }
    
    @Override
    public String encrypt(String message, String key) {
        if (!validateKey(key)) {
            return message;
        }
        
        int shift = calculateShift(key);
        StringBuilder encrypted = new StringBuilder();
        
        for (char c : message.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                encrypted.append((char) ((c - base + shift) % 26 + base));
            } else {
                encrypted.append(c);
            }
        }
        
        return encrypted.toString();
    }
    
    @Override
    public String decrypt(String encryptedMessage, String key) {
        if (!validateKey(key)) {
            return encryptedMessage;
        }
        
        int shift = calculateShift(key);
        StringBuilder decrypted = new StringBuilder();
        
        for (char c : encryptedMessage.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                decrypted.append((char) ((c - base - shift + 26) % 26 + base));
            } else {
                decrypted.append(c);
            }
        }
        
        return decrypted.toString();
    }
    
    private int calculateShift(String key) {
        int sum = 0;
        for (char c : key.toCharArray()) {
            sum += c;
        }
        return sum % 26;
    }
    
    @Override
    public boolean validateKey(String key) {
        return super.validateKey(key) && key.length() > 0;
    }
}
