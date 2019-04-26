package com.educative.service;

import com.educative.dao.User;
import com.educative.pojos.XUser;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private Mapper mapper;

    public User addUser(XUser user) {
        User userDetail = mapper.map(user, User.class);
        System.out.println(userDetail);
        return userDetail;
    }
}
