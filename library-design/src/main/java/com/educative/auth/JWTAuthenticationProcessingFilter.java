package com.educative.auth;

import com.educative.jwt.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;

public class JWTAuthenticationProcessingFilter extends OncePerRequestFilter {

    // NOTE AuthenticationManager must contains JWTAuthenticationProvider for JWT authentication purpose.
    private AuthenticationManager authenticationManager;
    private AuthenticationEntryPoint authenticationEntryPoint;

    public JWTAuthenticationProcessingFilter(
            @NotNull AuthenticationManager manager,
            @NotNull AuthenticationEntryPoint entryPoint) {
        Assert.notNull(manager, "authenticationManager cannot be null");
        Assert.notNull(entryPoint, "authenticationEntryPoint cannot be null");
        this.authenticationManager = manager;
        this.authenticationEntryPoint = entryPoint;
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        String token = JWTUtil.extractJWTToken(request);
        if (token == null) {
            // shouldn't happen
            return null;
        }
        JWTAuthenticationToken authRequest = new JWTAuthenticationToken(token);
        return authenticationManager.authenticate(authRequest);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        if (!authenticationIsRequired()) {
            chain.doFilter(request, response);
            return;
        }

        String token = JWTUtil.extractJWTToken(request);
        if (token == null) {
            chain.doFilter(request, response);
            return;
        }

        try {
            JWTAuthenticationToken jwtAuthenticationToken = new JWTAuthenticationToken(token);
            //
            // jwtAuthenticationToken.setDetails(this.authenticationDetailsSource.buildDetails(request));
            //
            Authentication authentication = authenticationManager.authenticate(jwtAuthenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            onSuccessfulAuthentication(request, response, authentication);
        } catch (AuthenticationException failed) {
            SecurityContextHolder.clearContext();
            onUnsuccessfulAuthentication(request, response, failed);
            authenticationEntryPoint.commence(request, response, failed);
            return;
        }

        chain.doFilter(request, response);
    }


    /**
     * @return true if configuration is set to require JWT Authentication, otherwise false.
     */
    private boolean authenticationIsRequired() {
        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
        return existingAuth == null || !existingAuth.isAuthenticated();
    }

    private void onSuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authResult) {
    }

    private void onUnsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed) {
        // TODO Log request IP, timestamp, token.
        logger.error(failed.getMessage());
    }
}
