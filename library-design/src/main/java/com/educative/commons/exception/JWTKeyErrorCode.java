package com.educative.commons.exception;

public class JWTKeyErrorCode extends ErrorCode{

    public static final JWTKeyErrorCode KEY_NOT_FOUND = getInstance(JWTKeyError.KEY_NOT_FOUND);
    public static final JWTKeyErrorCode KEY_EXPIRED = getInstance(JWTKeyError.KEY_EXPIRED);
    public static final JWTKeyErrorCode COULD_NOT_GENERATE_KEY = getInstance(JWTKeyError.COULD_NOT_GENERATE_KEY);

    public static JWTKeyErrorCode getInstance(JWTKeyError code) {
        return new JWTKeyErrorCode(code.getCode(), code.getMessage());
    }

    private JWTKeyErrorCode(int code, String message) {
        super(code, message);
    }
}
