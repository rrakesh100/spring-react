package com.educative.auth;

import com.educative.dao.LoginAttempt;
import com.educative.repository.LoginAttemptRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class LoginAttemptServiceImpl implements LoginAttemptService {

    private static final Logger _logger = LoggerFactory.getLogger(LoginAttemptServiceImpl.class);

    @Autowired
    private LoginAttemptRepository loginAttemptRepository;

    private int attemptLimit = 3;
    private long resetMinutes = 10;

    @Override
    public void increaseFailedAttemptCount(Long userId) {
        try {
            LoginAttempt loginAttempt = loginAttemptRepository.findById(userId).get();
            if(loginAttempt != null) {
                if(isQualifiedForReset(loginAttempt)) {
                    loginAttempt.setAttempts(1);
                } else {
                    loginAttempt.increaseAttempt();
                }
            } else {
                loginAttempt = new LoginAttempt();
                loginAttempt.setUserId(userId);
                loginAttempt.setAttempts(1);
            }
            loginAttempt.setLastUpdated(new Date());
            loginAttemptRepository.save(loginAttempt);
        } catch(Exception ex) {
            // swallow the exception and don't bubble up to break up the authentication since this is not critical
            _logger.error(ex.getMessage(), ex);
        }
    }

    private boolean isQualifiedForReset(LoginAttempt loginAttempt) {
        Date lastUpdated = loginAttempt.getLastUpdated();
        long diff = System.currentTimeMillis() - lastUpdated.getTime();
        if(diff > 60000 * resetMinutes) {
            // auto unlock after reset minutes
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void resetFailedAttemptCount(Long userId) {

        try {
            LoginAttempt loginAttempt = loginAttemptRepository.findById(userId).get();
            if(loginAttempt != null) {
                loginAttempt.setAttempts(0);
                loginAttempt.setLastUpdated(new Date());
                loginAttemptRepository.save(loginAttempt);
            }
        } catch(Exception ex) {
            // swallow the exception and don't bubble up to break up the authentication since this is not critical
            _logger.error(ex.getMessage(), ex);
        }

    }
}
