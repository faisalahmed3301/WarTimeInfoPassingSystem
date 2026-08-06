package wartimeinfosystem.interfaces;

/**
 * Interface for encryption operations
 * Demonstrates abstraction
 */
public interface Encryptable {
    String encrypt(String message, String key);
    String decrypt(String encryptedMessage, String key);
    String getEncryptionName();
}
