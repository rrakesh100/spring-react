package com.educative.commons.exception;

import java.util.ArrayList;
import java.util.List;

public class ApiException extends RuntimeException {

    public ApiException(){

    }

    protected List<ExceptionModel> errors = new ArrayList<ExceptionModel>();

    public ApiException(ErrorCode errorCode) {
        this.errors.add(new ExceptionModel(errorCode.getCode(), errorCode.getMessage()));
    }

    public List<ExceptionModel> getErrors() {
        return errors;
    }

}
