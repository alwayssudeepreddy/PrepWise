package com.prepwise.prepwise_backend.exception;

/**
 * Thrown when authentication fails (bad email/password). Mapped to HTTP 401.
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
