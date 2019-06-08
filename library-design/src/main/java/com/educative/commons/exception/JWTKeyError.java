package com.educative.commons.exception;

public enum JWTKeyError implements ErrorDefinition {

    KEY_NOT_FOUND(1001, "key not found"),
    KEY_EXPIRED(1002, "key has been expired"),
    COULD_NOT_GENERATE_KEY(1003, "could not generate a key");

    private int code;
    private String msg;

    JWTKeyError(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
