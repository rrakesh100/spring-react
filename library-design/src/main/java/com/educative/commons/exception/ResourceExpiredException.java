package com.educative.commons.exception;

public class ResourceExpiredException extends ApiException {

    private static final long serialVersionUID = 1l;

    public ResourceExpiredException(){

    }

    public ResourceExpiredException(ErrorCode errorCode){
        super(errorCode);
    }


}
