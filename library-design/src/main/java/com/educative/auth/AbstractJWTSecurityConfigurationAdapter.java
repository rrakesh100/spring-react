package com.educative.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractJWTSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

    @Override
    public final UserDetailsService userDetailsService() {
        return null;
    }

    public Http401UnauthorizedEntryPoint http401UnauthorizedEntryPoint() {
        return new Http401UnauthorizedEntryPoint();
    }

    public JWTAuthenticationProvider authenticationProvider() {
        JWTAuthenticationProvider provider = new JWTAuthenticationProvider();
        provider.setJWTTokenService(jwtTokenService());
        return provider;
    }

    public JWTAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        return new JWTAuthenticationProcessingFilter(
                authenticationManager(),
                http401UnauthorizedEntryPoint()
        );
    }

    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(authenticationProvider());
        return new ProviderManager(providers);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(http401UnauthorizedEntryPoint());

        // filter
        http.addFilterBefore(jwtAuthenticationProcessingFilter(), AbstractPreAuthenticatedProcessingFilter.class);
        // disable session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        configureUrlPattern(http);
    }

    abstract protected void configureUrlPattern(HttpSecurity http) throws Exception;

    abstract protected JWTTokenService jwtTokenService();
}