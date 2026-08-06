package com.wartime.system.exception;

/**
 * Root exception for all application-specific runtime errors in the
 * War-Time Information Passing System.
 * All domain exceptions extend this class to allow unified catch blocks
 * where needed.
 */
public class WarTimeException extends RuntimeException {

    public WarTimeException(String message) {
        super(message);
    }

    public WarTimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
