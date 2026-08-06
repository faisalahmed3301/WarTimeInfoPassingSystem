package com.wartime.system.exception;

/**
 * Thrown when an unrecognised encryption strategy name is provided.
 * For example: requesting "ROT13 Cipher" when only Caesar, Reverse, XOR, and Base64 are supported.
 */
public class UnknownStrategyException extends WarTimeException {

    public UnknownStrategyException(String message) {
        super(message);
    }

    public UnknownStrategyException(String message, Throwable cause) {
        super(message, cause);
    }
}
