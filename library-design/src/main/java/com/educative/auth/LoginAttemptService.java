package com.educative.auth;

public interface LoginAttemptService {

    public void increaseFailedAttemptCount(Long userId);

    public void resetFailedAttemptCount(Long userId);

}
