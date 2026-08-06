package com.wartime.system.exception;

/**
 * Thrown when the decryption key supplied by the user does not match the key
 * stored with the encrypted message.
 */
public class InvalidDecryptionKeyException extends DecryptionException {

    public InvalidDecryptionKeyException(String message) {
        super(message);
    }

    public InvalidDecryptionKeyException(String message, Throwable cause) {
        super(message, cause);
    }
}
