package com.educative.auth;

import com.educative.dao.Role;
import com.educative.dao.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultJHipUserDetails implements JHipAuthUserDetails {


    private User user;

    private List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

    public DefaultJHipUserDetails(User user){
        List<String> rolesList = new ArrayList();
        user.getGroups().forEach(group -> {
            Set<Role> roleSet = group.getRoles();
            List<String> l = roleSet.stream().map(new Function<Role, String>() {

                @Override
                public String apply(Role role) {
                    return role.getName();
                }
            }).collect(Collectors.toList());
            rolesList.addAll(l);
        });

        if(rolesList !=null) {
            rolesList.forEach(role -> {
                authorities.add( new SimpleGrantedAuthority("ROLE_" + role));
            });
        }

    }


    @Override
    public String[] getRoles() {

        List<String> rolesList = new ArrayList();
        user.getGroups().forEach(group -> {
            Set<Role> roleSet = group.getRoles();
            List<String> l = roleSet.stream().map(new Function<Role, String>() {

                @Override
                public String apply(Role role) {
                    return role.getName();
                }
            }).collect(Collectors.toList());
            rolesList.addAll(l);
        });

        if(rolesList !=null)
            return rolesList.toArray(new String[0]);
        else
            return new String[0];
    }

    @Override
    public Long getUserId() {
        return user.getId();
    }

    @Override
    public Long getTenantId() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getId().toString();
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
    public User getUserAccount() {
        return user;
    }
}
