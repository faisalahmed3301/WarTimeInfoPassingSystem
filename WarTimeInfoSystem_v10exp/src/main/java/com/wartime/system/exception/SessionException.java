package com.wartime.system.exception;

/**
 * Thrown when an operation is attempted without an active authenticated session.
 * For example: sending a message when no user is currently logged in.
 */
public class SessionException extends WarTimeException {

    public SessionException(String message) {
        super(message);
    }

    public SessionException(String message, Throwable cause) {
        super(message, cause);
    }
}
