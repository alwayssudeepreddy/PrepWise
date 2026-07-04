package com.prepwise.prepwise_backend.exception;

/**
 * Thrown when an authenticated user attempts an action they are not allowed to perform.
 * Mapped to HTTP 403.
 */
public class ForbiddenActionException extends RuntimeException {
    public ForbiddenActionException(String message) {
        super(message);
    }
}
