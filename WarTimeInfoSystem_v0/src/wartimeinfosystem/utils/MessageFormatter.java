package wartimeinfosystem.utils;

import wartimeinfosystem.models.Message;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility for formatting messages
 */
public class MessageFormatter {
    
    public static String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(timestamp));
    }
    
    public static String formatMessagePreview(Message message) {
        if (message == null) {
            return "";
        }
        
        return String.format("[%s] From: %s [%s]", 
            formatTimestamp(message.getTimestamp()),
            message.getSenderName(),
            message.getSenderAppointment().getDisplayName());
    }
    
    public static String maskMessage() {
        return "******";
    }
    
    public static String formatDecryptedMessage(Message message, String decryptedText) {
        StringBuilder sb = new StringBuilder();
        sb.append("===== DECRYPTED MESSAGE =====\n");
        sb.append("From: ").append(message.getSenderName()).append("\n");
        sb.append("Rank: ").append(message.getSenderAppointment()).append("\n");
        sb.append("Time: ").append(formatTimestamp(message.getTimestamp())).append("\n");
        sb.append("Encryption: ").append(message.getEncryptionType()).append("\n");
        sb.append("=============================\n\n");
        sb.append(decryptedText);
        return sb.toString();
    }
}
