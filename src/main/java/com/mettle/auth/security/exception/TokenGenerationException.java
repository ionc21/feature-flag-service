package com.mettle.auth.security.exception;

public class TokenGenerationException extends RuntimeException {

    public TokenGenerationException(String message, Exception cause) {
        super(message, cause);
    }
}
