package com.educative.controllers;

import com.educative.dao.User;
import com.educative.pojos.XUser;
import com.educative.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/v1/")
public class UsersController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public @ResponseBody
    void addUser(@RequestBody XUser user) throws Exception {

      User userDetail = userService.addUser(user);
      //return userDetail ;
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public @ResponseBody
    XUser getUser() throws Exception {

        XUser user = new XUser();
        user.setFirstName("rakesh");
        user.setLastName("ram");

        return user ;
    }




}
