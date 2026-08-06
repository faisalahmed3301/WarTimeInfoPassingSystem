package com.wartime.system.util;

import com.wartime.system.exception.IncompleteTransmissionException;
import com.wartime.system.model.AbstractUser;
import com.wartime.system.model.Group;

/**
 * Utility class dedicated to validating transmission data before sending.
 * 
 * OOP Concept: Encapsulation and Single Responsibility Principle.
 * All validation logic is encapsulated within this class, separating it from the 
 * UI controller (SenderController) and the business logic (MessageService).
 */
public class TransmissionValidator {

    /**
     * Validates that all required fields for a transmission are present.
     * Throws an IncompleteTransmissionException if any validation fails.
     * 
     * @param message          The message content to send.
     * @param encryptionMethod The selected cipher method.
     * @param encryptionKey    The secret key for encryption.
     * @param targetGroup      The selected target group (if any).
     * @param targetUser       The selected target individual (if any).
     * @param appointmentRank  The selected target rank (if any).
     * @throws IncompleteTransmissionException if validation fails.
     */
    public static void validate(String message, String encryptionMethod, String encryptionKey,
                                Group targetGroup, AbstractUser targetUser, String appointmentRank)
            throws IncompleteTransmissionException {

        if (message == null || message.trim().isEmpty()) {
            throw new IncompleteTransmissionException("Transmission Failure : The message content cannot be empty.");
        }

        if (encryptionMethod == null || encryptionMethod.trim().isEmpty()) {
            throw new IncompleteTransmissionException("Transmission Failure: You must select a secure encryption method.");
        }

        if (encryptionKey == null || encryptionKey.trim().isEmpty()) {
            throw new IncompleteTransmissionException("Transmission Failure: An encryption key is required to secure the transmission.");
        }

        // At least one target must be selected (Group, User, or Rank broadcast)
        if (targetGroup == null && targetUser == null && appointmentRank == null) {
            throw new IncompleteTransmissionException("Transmission Failure: No target specified. Please select a valid Group, Individual Recipient, or Authorizing Appointment.");
        }
    }
}
