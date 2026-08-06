package com.wartime.system.exception;

/**
 * Thrown when the EncryptionContext is asked to encrypt or decrypt data
 * before an EncryptionStrategy has been configured.
 * Replaces the raw {@link IllegalStateException} previously used in EncryptionContext.
 */
public class InvalidStrategyStateException extends EncryptionException {

    public InvalidStrategyStateException(String message) {
        super(message);
    }

    public InvalidStrategyStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
