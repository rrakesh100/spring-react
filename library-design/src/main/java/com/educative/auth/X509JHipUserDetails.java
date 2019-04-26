package com.educative.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class X509JHipUserDetails  implements JHipUserDetails {


    private String[] roles;

    private Collection<? extends GrantedAuthority> authorities;

    private String serviceName; //CCM_BACKEND, CCM_UI

    public X509JHipUserDetails(String serviceName) {
        this.serviceName = serviceName;
        roles = new String[] { "INTERNAL_SERVICE", "INTERNAL_SERVICE_" + serviceName.toUpperCase() };
        this.authorities = Arrays.stream(roles).map(s -> new SimpleGrantedAuthority("ROLE_" + s))
                .collect(Collectors.toList());
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
        return serviceName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String[] getRoles() {
        return roles;
    }

    @Override
    public Long getUserId() {
        return null;
    }

    @Override
    public Long getTenantId() {
        return null;
    }
}
