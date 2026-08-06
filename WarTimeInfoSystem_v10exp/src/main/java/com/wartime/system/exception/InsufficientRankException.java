package com.wartime.system.exception;

/**
 * Thrown when a user's rank is too low to perform a requested operation.
 * For example: a Soldier trying to send an emergency alert (Commander-only action).
 */
public class InsufficientRankException extends UnauthorizedAccessException {

    public InsufficientRankException(String message) {
        super(message);
    }

    public InsufficientRankException(String message, Throwable cause) {
        super(message, cause);
    }
}