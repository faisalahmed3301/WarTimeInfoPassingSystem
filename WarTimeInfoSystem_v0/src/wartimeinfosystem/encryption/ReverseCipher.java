package wartimeinfosystem.encryption;

/**
 * Reverse Cipher implementation
 * Demonstrates polymorphism through inheritance
 */
public class ReverseCipher extends EncryptionAlgorithm {
    
    public ReverseCipher() {
        super("Reverse Cipher");
    }
    
    @Override
    public String encrypt(String message, String key) {
        if (!validateKey(key)) {
            return message;
        }
        
        // Reverse and apply key-based character shift
        StringBuilder encrypted = new StringBuilder(message);
        encrypted.reverse();
        
        int keyValue = key.hashCode() % 100;
        StringBuilder result = new StringBuilder();
        
        for (char c : encrypted.toString().toCharArray()) {
            result.append((char) (c + keyValue));
        }
        
        return result.toString();
    }
    
    @Override
    public String decrypt(String encryptedMessage, String key) {
        if (!validateKey(key)) {
            return encryptedMessage;
        }
        
        int keyValue = key.hashCode() % 100;
        StringBuilder intermediate = new StringBuilder();
        
        for (char c : encryptedMessage.toCharArray()) {
            intermediate.append((char) (c - keyValue));
        }
        
        return intermediate.reverse().toString();
    }
}
