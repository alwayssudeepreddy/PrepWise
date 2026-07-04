package com.prepwise.prepwise_backend.exception;

/**
 * Thrown for invalid client requests that are not covered by bean validation
 * (e.g. resubmitting a completed test). Mapped to HTTP 400.
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
