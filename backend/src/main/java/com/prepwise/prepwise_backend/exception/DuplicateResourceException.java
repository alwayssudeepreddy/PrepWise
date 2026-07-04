package com.prepwise.prepwise_backend.exception;

/**
 * Thrown when creating an entity that violates a uniqueness constraint. Mapped to HTTP 409.
 */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
