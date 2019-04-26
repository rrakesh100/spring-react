package com.educative.auth;

import com.educative.dao.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class DefaultAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    private static final Logger _logger = LoggerFactory.getLogger(DefaultAuthenticationSuccessHandler.class.getName());
    private UserAccountService userAccountService;

    private LoginAttemptService loginAttemptService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
            if(authentication.getPrincipal() instanceof JHipAuthUserDetails) {
                JHipAuthUserDetails authUserDetails = (JHipAuthUserDetails) authentication.getPrincipal();

                User user = authUserDetails.getUserAccount();
               // user.setLastLoginTime= userAccountService.update(user)

                loginAttemptService.resetFailedAttemptCount(user.getId());
            }else {
                _logger.warn("Principal is not instance of JHipUserDetails", authentication);
            }
    }

    public void setUserAccountService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    public void setLoginAttemptService(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }
}
