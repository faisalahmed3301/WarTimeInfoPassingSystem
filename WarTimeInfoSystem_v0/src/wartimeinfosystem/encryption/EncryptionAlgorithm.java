package wartimeinfosystem.encryption;

import wartimeinfosystem.interfaces.Encryptable;

/**
 * Abstract base class for encryption algorithms
 * Demonstrates abstraction and inheritance
 */
public abstract class EncryptionAlgorithm implements Encryptable {
    protected String algorithmName;
    
    public EncryptionAlgorithm(String algorithmName) {
        this.algorithmName = algorithmName;
    }
    
    @Override
    public String getEncryptionName() {
        return algorithmName;
    }
    
    // Template method - can be overridden
    public boolean validateKey(String key) {
        return key != null && !key.isEmpty();
    }
    
    // Abstract methods to be implemented by subclasses
    @Override
    public abstract String encrypt(String message, String key);
    
    @Override
    public abstract String decrypt(String encryptedMessage, String key);
}
