package com.educative.auth;

import com.educative.dao.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DefaultJHipUserDetailService implements UserDetailsService {

    private UserAccountService userAccountService;

    public void setLoginAttemptService(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    private LoginAttemptService loginAttemptService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User u =   userAccountService.getUserByEmail(s);
        DefaultJHipUserDetails userDetails = new DefaultJHipUserDetails(u);
        return  userDetails;
    }

    public void setUserAccountService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }
}
