package wartimeinfosystem.enums;

/**
 * Enum for encryption methods
 */
public enum EncryptionType {
    CAESAR_CIPHER("Caesar Cipher"),
    REVERSE_CIPHER("Reverse Cipher"),
    XOR_CIPHER("XOR Cipher"),
    SUBSTITUTION_CIPHER("Substitution Cipher");
    
    private final String displayName;
    
    private EncryptionType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
