package com.educative.auth;

import org.springframework.security.core.Authentication;

public interface JHipAuthentication extends Authentication {

    JHipUserDetails getCPSGUserDetails();

}