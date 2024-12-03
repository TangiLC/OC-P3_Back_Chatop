package com.chatop.exception;

/**
 * Exception thrown when a file fails validation.
 */
public class FileValidationException extends RuntimeException {
    public FileValidationException(String message) {
        super(message);
    }

    public FileValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
