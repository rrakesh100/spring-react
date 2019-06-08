package com.educative.commons.exception;

/**
 * Definition for API error
 */
public interface ErrorDefinition {

    /**
     * @return error code
     */
    int getCode();

    /**
     * @return error description
     */
    String getMessage();
}
