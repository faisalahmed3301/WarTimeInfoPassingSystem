package com.wartime.system.exception;

/**
 * Thrown when a message transmission is attempted with missing or invalid fields.
 * For example: empty message body, no encryption method selected, or no target specified.
 * This is a <strong>checked</strong> exception — callers must declare or handle it
 * to enforce explicit validation at compile time.
 */
public class IncompleteTransmissionException extends Exception {

    public IncompleteTransmissionException(String message) {
        super(message);
    }

    public IncompleteTransmissionException(String message, Throwable cause) {
        super(message, cause);
    }
}
