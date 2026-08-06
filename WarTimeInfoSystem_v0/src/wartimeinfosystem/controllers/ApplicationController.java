package wartimeinfosystem.controllers;

/**
 * Singleton Application Controller
 * Manages global application state
 */
public class ApplicationController {
    private static ApplicationController instance;
    private static MessageController messageController;
    private static EncryptionController encryptionController;
    
    private ApplicationController() {
        messageController = new MessageController();
        encryptionController = new EncryptionController();
    }
    
    public static ApplicationController getInstance() {
        if (instance == null) {
            instance = new ApplicationController();
        }
        return instance;
    }
    
    public static MessageController getMessageController() {
        if (messageController == null) {
            messageController = new MessageController();
        }
        return messageController;
    }
    
    public static EncryptionController getEncryptionController() {
        if (encryptionController == null) {
            encryptionController = new EncryptionController();
        }
        return encryptionController;
    }
}
