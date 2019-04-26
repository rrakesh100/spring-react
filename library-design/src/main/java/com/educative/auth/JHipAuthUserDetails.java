package com.educative.auth;

import com.educative.dao.User;

public interface JHipAuthUserDetails extends JHipUserDetails {

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_INTERNAL = "ROLE_INTERNAL";
    public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

    public User getUserAccount();

}
