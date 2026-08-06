package wartimeinfosystem.interfaces;

import wartimeinfosystem.models.Message;
import java.util.List;

/**
 * Interface for message operations
 */
public interface MessageOperations {
    void sendMessage(Message message);
    List<Message> getMessages();
    void removeMessage(Message message);
}
