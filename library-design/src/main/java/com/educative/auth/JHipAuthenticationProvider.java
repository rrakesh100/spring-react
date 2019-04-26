package com.educative.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JHipAuthenticationProvider extends DaoAuthenticationProvider {

    private static final Logger _logger = LoggerFactory.getLogger(JHipAuthenticationProvider.class);


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        Authentication result = super.authenticate(authentication);

//        if(result != null && result instanceof UsernamePasswordAuthenticationFilter &&
//            result.getPrincipal() !=null && result.getPrincipal().getClass().equals(DefaultJHipUserDetails.class)) {
//
//        }

        return result;
    }
}
