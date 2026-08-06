package com.wartime.system.exception;

/**
 * Thrown when a user attempts an operation they are not authorised to perform.
 * Sub-types: {@link InsufficientRankException}
 */
public class UnauthorizedAccessException extends WarTimeException {

    public UnauthorizedAccessException(String message) {
        super(message);
    }

    public UnauthorizedAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
