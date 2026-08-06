
import com.wartime.system.model.*;
import com.wartime.system.service.*;
import com.wartime.system.security.*;
import com.wartime.system.exception.WarTimeException;

public class ReproductionCheck {
    public static void main(String[] args) {
        try {
            MessageService messageService = MessageService.getInstance();
            AuthenticationService auth = AuthenticationService.getInstance();
            
            // Setup a commander user
            AbstractUser commander = UserFactory.createUser("cmd1", "COMMANDER");
            SecurityContext.getInstance().setCurrentUser(commander);
            
            // Create a secure message
            String plainText = "Secret Mission Data";
            String correctKey = "KEY123";
            String wrongKey = "WRONG";
            
            messageService.sendMessage(plainText, correctKey, "Caesar Cipher", "COMMANDER", null, null, "NORMAL");
            SecureMessage msg = messageService.getAllMessages().get(0);
            
            System.out.println("Message encrypted with key: " + correctKey);
            
            // Try decrypting with wrong key
            try {
                System.out.println("Attempting decryption with WRONG key...");
                messageService.attemptDecryption(msg, wrongKey);
                System.err.println("FAILED: Decryption succeeded with WRONG key!");
                System.exit(1);
            } catch (WarTimeException e) {
                System.out.println("SUCCESS: Decryption failed as expected with message: " + e.getMessage());
            }
            
            // Try decrypting with correct key
            try {
                System.out.println("Attempting decryption with CORRECT key...");
                String decrypted = messageService.attemptDecryption(msg, correctKey);
                System.out.println("Decrypted text: " + decrypted);
                if (plainText.equals(decrypted)) {
                    System.out.println("SUCCESS: Decryption succeeded with correct key.");
                } else {
                    System.err.println("FAILED: Decrypted text does not match!");
                    System.exit(1);
                }
            } catch (Exception e) {
                System.err.println("FAILED: Decryption failed with CORRECT key: " + e.getMessage());
                System.exit(1);
            }
            
            System.out.println("\nALL CHECKS PASSED");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
