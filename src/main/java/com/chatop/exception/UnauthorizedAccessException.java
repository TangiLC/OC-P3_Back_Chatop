package com.chatop.exception;

/**
 * Exception thrown when a user tries to access a resource they do not own.
 */
public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
