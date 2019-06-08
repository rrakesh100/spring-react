package com.educative.auth;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

public class JWTUserDetails implements JHipUserDetails {

    private static final long serialVersionUID = 1L;
    private static final Logger _logger = LoggerFactory.getLogger(JWTUserDetails.class);

    private static final String PREFIX = "SUITE_";

    private DecodedJWT jwt;
    private Long userId;
    private Long tenantId;
    private String tokenId;
    private String roles[];
    Collection<? extends GrantedAuthority> authorities;

    public JWTUserDetails(DecodedJWT jwt) {
        this.jwt = jwt;
        _logger.debug(jwt.getSubject());
        init(jwt);
    }

    private void init(DecodedJWT jwt) {
        userId = getUserId(jwt);
        tenantId = getTenantId(jwt.getClaim("tenantId"));
        roles = getRoles(jwt.getClaim("rolez"));
        tokenId = jwt.getId();
        authorities = getAuthorities(roles);
    }

    // return only roles starting with prefix
    private String[] filterRoles(String roles[]) {
        int n = PREFIX.length();
        Set<String> results = new HashSet<String>();
        for(String role : roles) {
            if(role.startsWith(PREFIX)) {
                results.add(role.substring(n));
            }
        }
        return results.toArray(new String[results.size()]);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String roles[]) {
        if (roles == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(roles)
                .map(s -> new SimpleGrantedAuthority("ROLE_" + s))
                .collect(Collectors.toList());
    }

    private String[] getRoles(Claim roles) {
        if (roles == null) {
            return new String[0];
        }
        return filterRoles(roles.asArray(String.class));
    }

    private Long getTenantId(Claim tenantId) {
        if (tenantId != null) {
            try {
                return Long.valueOf(tenantId.asString());
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    private Long getUserId(DecodedJWT jwt) {
        try {
            return Long.valueOf(jwt.getSubject());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public DecodedJWT getJWT() {
        return jwt;
    }

    @Override
    public String[] getRoles() {
        return roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return jwt.getSubject();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public Long getTenantId() {
        return tenantId;
    }

    public String getTokenId() {
        return tokenId;
    }
}


