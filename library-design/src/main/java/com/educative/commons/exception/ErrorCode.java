package com.educative.commons.exception;

public abstract class ErrorCode {

    private final int errorCode;
    private final String errorMessage;

    protected ErrorCode(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getCode() {
        return errorCode;
    }

    public String getMessage() {
        return errorMessage;
    }
}
