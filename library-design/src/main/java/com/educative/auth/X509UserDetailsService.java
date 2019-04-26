package com.educative.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class X509UserDetailsService implements UserDetailsService {

    private static final Logger _logger = LoggerFactory.getLogger(X509UserDetailsService.class);


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        _logger.debug("username: " + username);
        return new X509JHipUserDetails(username);    }
}
