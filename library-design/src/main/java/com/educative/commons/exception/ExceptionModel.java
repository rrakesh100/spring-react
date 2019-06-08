package com.educative.commons.exception;

import java.util.List;

public class ExceptionModel {

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    private int errorCode;

    private String errorMessage;

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    private List<String> arguments;

    public ExceptionModel(int errorCode, String errorMessage) {
        this(errorCode, errorMessage, null);
    }

    public ExceptionModel(int errorCode, String errorMessage, List<String> arguments) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.arguments = arguments;
    }





}
