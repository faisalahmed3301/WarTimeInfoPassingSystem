package wartimeinfosystem.security;

import wartimeinfosystem.enums.Appointment;
import wartimeinfosystem.models.Message;
import wartimeinfosystem.models.User;

/**
 * Handles access control and authorization
 */
public class AccessControl {
    private User currentUser;
    
    public AccessControl(User user) {
        this.currentUser = user;
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    public boolean canAccessMessage(Message message) {
        if (currentUser == null || message == null) {
            return false;
        }
        
        Appointment userAppointment = currentUser.getAppointment();
        Appointment senderAppointment = message.getSenderAppointment();
        
        return userAppointment.canReadMessage(senderAppointment);
    }
    
    public boolean hasHigherOrEqualRank(Appointment targetAppointment) {
        if (currentUser == null || targetAppointment == null) {
            return false;
        }
        
        return currentUser.getAppointment().getLevel() >= targetAppointment.getLevel();
    }
    
    public String getAccessLevel() {
        if (currentUser == null) {
            return "No Access";
        }
        
        return currentUser.getAppointment().getDisplayName() + " Level";
    }
}
