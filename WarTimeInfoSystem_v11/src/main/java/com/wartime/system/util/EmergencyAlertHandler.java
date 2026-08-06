package com.wartime.system.util;

import com.wartime.system.exception.IncompleteTransmissionException;
import com.wartime.system.service.MessageService;

/**
 * Utility class dedicated to handling emergency broadcasts.
 * 
 * OOP Concept: Delegation and Single Responsibility Principle.
 * The UI controller delegates the business logic of an emergency broadcast to this class.
 */
public class EmergencyAlertHandler {

    /**
     * Broadcasts an emergency alert to all personnel.
     * 
     * @param message The emergency message to send.
     * @throws IncompleteTransmissionException if the message is empty.
     */
    public static void broadcastEmergency(String message) throws IncompleteTransmissionException {
        if (message == null || message.trim().isEmpty()) {
            // Reusing our custom exception for consistent error handling
            throw new IncompleteTransmissionException("Please enter a message for the emergency alert.");
        }
        
        // Execute the business logic via the specialized service
        MessageService.getInstance().sendEmergencyMessage(message);
    }
}
