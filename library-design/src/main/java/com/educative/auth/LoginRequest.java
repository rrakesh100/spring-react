package com.educative.auth;


import lombok.Getter;
import lombok.Setter;

public class LoginRequest {


    @Getter @Setter
    private String password;

    @Getter @Setter
    private String tenantname;

    @Getter @Setter
    private String username;

}
