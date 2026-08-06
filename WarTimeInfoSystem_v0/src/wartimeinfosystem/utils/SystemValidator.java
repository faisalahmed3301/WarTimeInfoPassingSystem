package wartimeinfosystem.utils;

/**
 * Utility class for validation
 */
public class SystemValidator {
    
    public static boolean validateUsername(String username) {
        return username != null && !username.trim().isEmpty() && username.length() >= 3;
    }
    
    public static boolean validatePassword(String password) {
        return password != null && !password.trim().isEmpty() && password.length() >= 4;
    }
    
    public static boolean validateMessage(String message) {
        return message != null && !message.trim().isEmpty();
    }
    
    public static boolean validateKey(String key) {
        return key != null && !key.trim().isEmpty() && key.length() >= 3;
    }
    
    public static String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        return input.trim();
    }
}
