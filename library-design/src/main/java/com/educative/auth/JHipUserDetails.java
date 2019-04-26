package com.educative.auth;

import org.springframework.security.core.userdetails.UserDetails;

public interface JHipUserDetails extends UserDetails {

    public String[] getRoles();

    Long getUserId();

    Long getTenantId();
}
