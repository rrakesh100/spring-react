package com.educative.commons.exception;

public class JWTTokenException extends Exception {

    private static final long serialVersionUID = 1L;

    public JWTTokenException(String message) {
        super(message);
    }

    public JWTTokenException(String message, Throwable th) {
        super(message, th);
    }
}

