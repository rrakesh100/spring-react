package com.educative.auth;

import com.educative.dao.User;

public interface UserAccountService {

    public User getUserById(Long id);

    public User getUserByEmail(String email);

}
