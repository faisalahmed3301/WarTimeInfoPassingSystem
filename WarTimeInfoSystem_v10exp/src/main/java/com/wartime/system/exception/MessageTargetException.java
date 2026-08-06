package com.wartime.system.exception;

/**
 * Thrown when a message transmission is attempted without specifying a valid target.
 * A valid target is one of: an Appointment rank, a Group, or an individual user.
 */
public class MessageTargetException extends WarTimeException {

    public MessageTargetException(String message) {
        super(message);
    }

    public MessageTargetException(String message, Throwable cause) {
        super(message, cause);
    }
}
