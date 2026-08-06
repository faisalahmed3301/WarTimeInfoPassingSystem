package com.wartime.system.handler;

import com.wartime.system.exception.IncompleteTransmissionException;
import com.wartime.system.model.AbstractUser;
import com.wartime.system.model.Group;
import com.wartime.system.service.MessageService;
import com.wartime.system.util.EmergencyAlertHandler;
import com.wartime.system.util.TransmissionValidator;
import javafx.scene.control.Alert;


public class TransmissionExceptionHandler {

    /**
     * Attempts to send a standard transmission, handling any exceptions that arise.
     */
    public static boolean attemptSend(String msg, String key, String method, String appointment, 
                                   Group selectedGroup, AbstractUser selectedRecipient, String priority) {
        try {
            // 1. Validation Logic
            TransmissionValidator.validate(msg, method, key, selectedGroup, selectedRecipient, appointment);

            // 2. Execution Logic
            MessageService.getInstance().sendMessage(msg, key, method, appointment, selectedGroup, selectedRecipient, priority);
            
            // Success
            showAlert("Success", "Message Sent successfully!");
            return true;

        } catch (IncompleteTransmissionException e) {
            // OOP Exception Handling: Catching our Custom Checked Exception
            showAlert("Validation Error", e.getMessage());
            return false;

        } catch (Exception e) {
            // General Exception Handling: Catching unexpected runtime errors
            showAlert("Transmission Failed", "System Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Attempts to broadcast an emergency alert, handling any exceptions that arise.
     */
    public static boolean attemptEmergency(String msg) {
        try {
            // 1. Delegation Logic
            EmergencyAlertHandler.broadcastEmergency(msg);
            
            // Success
            showAlert("Emergency Alert Sent", "Emergency alert has been broadcast to all personnel!");
            return true;

        } catch (IncompleteTransmissionException e) {
            // OOP Exception Handling: Catching our Custom Checked Exception
            showAlert("Emergency Validation Error", e.getMessage());
            return false;

        } catch (Exception e) {
            // General Exception Handling: Catching unexpected runtime errors
            showAlert("Emergency Failed", "System Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Helper method to show alerts cleanly.
     */
    private static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
