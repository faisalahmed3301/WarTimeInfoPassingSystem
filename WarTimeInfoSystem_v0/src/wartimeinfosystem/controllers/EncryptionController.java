package wartimeinfosystem.controllers;

import wartimeinfosystem.encryption.*;
import wartimeinfosystem.enums.EncryptionType;

/**
 * Controller for encryption operations
 * Demonstrates polymorphism - using different encryption algorithms through common interface
 */
public class EncryptionController {
    private EncryptionAlgorithm currentAlgorithm;
    
    public EncryptionController() {
        this.currentAlgorithm = new CaesarCipher();
    }
    
    public void setEncryptionType(EncryptionType type) {
        // Polymorphism: different objects based on type
        switch (type) {
            case CAESAR_CIPHER:
                currentAlgorithm = new CaesarCipher();
                break;
            case REVERSE_CIPHER:
                currentAlgorithm = new ReverseCipher();
                break;
            case XOR_CIPHER:
                currentAlgorithm = new XORCipher();
                break;
            case SUBSTITUTION_CIPHER:
                currentAlgorithm = new SubstitutionCipher();
                break;
            default:
                currentAlgorithm = new CaesarCipher();
        }
    }
    
    public String encryptMessage(String message, String key, EncryptionType type) {
        setEncryptionType(type);
        return currentAlgorithm.encrypt(message, key);
    }
    
    public String decryptMessage(String encryptedMessage, String key, EncryptionType type) {
        setEncryptionType(type);
        return currentAlgorithm.decrypt(encryptedMessage, key);
    }
    
    public EncryptionAlgorithm getCurrentAlgorithm() {
        return currentAlgorithm;
    }
    
    public boolean verifyKey(String key) {
        return currentAlgorithm.validateKey(key);
    }
}
