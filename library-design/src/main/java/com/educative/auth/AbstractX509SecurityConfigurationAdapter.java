package com.educative.auth;

import org.springframework.http.HttpRequest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

public abstract class AbstractX509SecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

    @Override
    public UserDetailsService userDetailsService() {
        return new X509UserDetailsService();
    }

    public Http401UnauthorizedEntryPoint http401UnauthorizedEntryPoint() {
        return new Http401UnauthorizedEntryPoint();
    }

    public void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(http401UnauthorizedEntryPoint());

        http.x509()
                .subjectPrincipalRegex("CN=(.*?)(?:,|$)")
                .userDetailsService(userDetailsService());

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        configureUrlPattern(); //each service has a custom URL Pattern

    }

    public abstract  void configureUrlPattern();

}
