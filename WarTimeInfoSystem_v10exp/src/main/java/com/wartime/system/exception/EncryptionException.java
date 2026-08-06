package com.wartime.system.exception;

/**
 * Thrown when an error occurs during the encryption of a message.
 * Sub-types: {@link InvalidStrategyStateException}
 */
public class EncryptionException extends WarTimeException {

    public EncryptionException(String message) {
        super(message);
    }

    public EncryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
