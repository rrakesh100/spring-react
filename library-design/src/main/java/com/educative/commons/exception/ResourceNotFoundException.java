package com.educative.commons.exception;

public class ResourceNotFoundException extends ApiException {

    private static final long serialVersionUID = 1l;

    public ResourceNotFoundException(){

    }

    public ResourceNotFoundException(ErrorCode errorCode){
      super(errorCode);
    }



}
