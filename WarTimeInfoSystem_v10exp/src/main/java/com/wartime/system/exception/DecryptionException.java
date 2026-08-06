package com.wartime.system.exception;

/**
 * Thrown when an error occurs during the decryption of a message.
 * Sub-types: {@link InvalidDecryptionKeyException}
 */
public class DecryptionException extends WarTimeException {

    public DecryptionException(String message) {
        super(message);
    }

    public DecryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
