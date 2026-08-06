package wartimeinfosystem.controllers;

import wartimeinfosystem.models.Message;
import wartimeinfosystem.models.User;
import wartimeinfosystem.interfaces.MessageOperations;
import wartimeinfosystem.security.AccessControl;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for message operations
 * Demonstrates encapsulation and interface implementation
 */
public class MessageController implements MessageOperations {
    private List<Message> messageStore;
    private AccessControl accessControl;
    
    public MessageController() {
        this.messageStore = new ArrayList<>();
    }
    
    public void setAccessControl(AccessControl accessControl) {
        this.accessControl = accessControl;
    }
    
    @Override
    public void sendMessage(Message message) {
        if (message != null) {
            messageStore.add(message);
        }
    }
    
    @Override
    public List<Message> getMessages() {
        return new ArrayList<>(messageStore);
    }
    
    public List<Message> getAccessibleMessages(User user) {
        List<Message> accessible = new ArrayList<>();
        AccessControl ac = new AccessControl(user);
        
        for (Message msg : messageStore) {
            if (ac.canAccessMessage(msg) && !msg.isRead()) {
                accessible.add(msg);
            }
        }
        
        return accessible;
    }
    
    @Override
    public void removeMessage(Message message) {
        messageStore.remove(message);
    }
    
    public void markAsRead(Message message) {
        if (message != null) {
            message.setRead(true);
            removeMessage(message);
        }
    }
    
    public int getMessageCount() {
        return messageStore.size();
    }
    
    public void clearAllMessages() {
        messageStore.clear();
    }
}
