package wartimeinfosystem.encryption;

/**
 * XOR Cipher implementation
 */
public class XORCipher extends EncryptionAlgorithm {
    
    public XORCipher() {
        super("XOR Cipher");
    }
    
    @Override
    public String encrypt(String message, String key) {
        if (!validateKey(key)) {
            return message;
        }
        
        StringBuilder encrypted = new StringBuilder();
        int keyLength = key.length();
        
        for (int i = 0; i < message.length(); i++) {
            char messageChar = message.charAt(i);
            char keyChar = key.charAt(i % keyLength);
            encrypted.append((char) (messageChar ^ keyChar));
        }
        
        return encrypted.toString();
    }
    
    @Override
    public String decrypt(String encryptedMessage, String key) {
        // XOR is symmetric - encryption and decryption are the same
        return encrypt(encryptedMessage, key);
    }
}
