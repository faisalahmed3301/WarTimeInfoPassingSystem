package com.wartime.system.exception;

/**
 * Thrown when an unrecognised or malformed rank/appointment string is provided.
 * For example: passing "CAPITAN" when valid ranks are COMMANDER, OFFICER, SOLDIER.
 */
public class InvalidRankException extends WarTimeException {

    public InvalidRankException(String message) {
        super(message);
    }

    public InvalidRankException(String message, Throwable cause) {
        super(message, cause);
    }
}
