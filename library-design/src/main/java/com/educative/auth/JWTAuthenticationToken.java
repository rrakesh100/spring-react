package com.educative.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class JWTAuthenticationToken extends AbstractAuthenticationToken implements JHipAuthentication {
    private static final long serialVersionUID = 1L;

    private String jwtToken;
    private final JWTUserDetails userDetails;
    private Collection<GrantedAuthority> authorities;
    private DecodedJWT decodedJWT = null;

    public JWTAuthenticationToken(DecodedJWT decodedJWT) {
        super(null);
        this.jwtToken = decodedJWT.getToken();
        this.decodedJWT = decodedJWT;
        this.userDetails = null;
        super.setAuthenticated(false);
    }

    public JWTAuthenticationToken(String jwtToken) {
        super(null);
        this.jwtToken = jwtToken;
        this.userDetails = null;
        super.setAuthenticated(false);
    }

    public JWTAuthenticationToken(String jwtToken, JWTUserDetails details) {
        super(null);
        this.jwtToken = jwtToken;
        this.userDetails = details;
        this.authorities = Collections.unmodifiableCollection(details.getAuthorities());
        super.setAuthenticated(true);
    }

    public String getJWTToken() {
        return jwtToken;
    }

    public DecodedJWT getDecodedJWTToken() {
        if(decodedJWT == null) {
            decodedJWT = JWT.decode(jwtToken);
        }
        return decodedJWT;
    }

    @Override
    public Object getCredentials() {
        if (isAuthenticated()) {
            return jwtToken;
        }
        return null;
    }

    @Override
    public Object getDetails() {
        if (isAuthenticated()) {
            return userDetails;
        }
        return null;
    }

    @Override
    public Object getPrincipal() {
        return getDetails();
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        if (isAuthenticated()) {
            return authorities;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public JHipUserDetails getCPSGUserDetails() {
        return userDetails;
    }
}
