package wartimeinfosystem.models;

import wartimeinfosystem.enums.Appointment;
import wartimeinfosystem.enums.EncryptionType;

/**
 * Message class demonstrating encapsulation
 */
public class Message {
    private String originalMessage;
    private String encryptedMessage;
    private String encryptionKey;
    private EncryptionType encryptionType;
    private Appointment senderAppointment;
    private String senderName;
    private long timestamp;
    private boolean isRead;
    
    public Message() {
        this.timestamp = System.currentTimeMillis();
        this.isRead = false;
    }
    
    public Message(String originalMessage, String encryptedMessage, String encryptionKey, 
                   EncryptionType encryptionType, Appointment senderAppointment, String senderName) {
        this.originalMessage = originalMessage;
        this.encryptedMessage = encryptedMessage;
        this.encryptionKey = encryptionKey;
        this.encryptionType = encryptionType;
        this.senderAppointment = senderAppointment;
        this.senderName = senderName;
        this.timestamp = System.currentTimeMillis();
        this.isRead = false;
    }
    
    // Encapsulation: Getters
    public String getOriginalMessage() {
        return originalMessage;
    }
    
    public String getEncryptedMessage() {
        return encryptedMessage;
    }
    
    public String getEncryptionKey() {
        return encryptionKey;
    }
    
    public EncryptionType getEncryptionType() {
        return encryptionType;
    }
    
    public Appointment getSenderAppointment() {
        return senderAppointment;
    }
    
    public String getSenderName() {
        return senderName;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public boolean isRead() {
        return isRead;
    }
    
    // Encapsulation: Setters
    public void setOriginalMessage(String originalMessage) {
        this.originalMessage = originalMessage;
    }
    
    public void setEncryptedMessage(String encryptedMessage) {
        this.encryptedMessage = encryptedMessage;
    }
    
    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }
    
    public void setEncryptionType(EncryptionType encryptionType) {
        this.encryptionType = encryptionType;
    }
    
    public void setSenderAppointment(Appointment senderAppointment) {
        this.senderAppointment = senderAppointment;
    }
    
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    
    public void setRead(boolean read) {
        isRead = read;
    }
    
    public String getMaskedMessage() {
        return "******";
    }
}
