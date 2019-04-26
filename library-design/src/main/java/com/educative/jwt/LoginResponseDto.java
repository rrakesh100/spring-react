package com.educative.jwt;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class LoginResponseDto {

    @Getter @Setter
    private String token;

    @Getter @Setter
    private String csrfToken;

    @Getter @Setter
    private Date issuedAt;

    @Getter @Setter
    private Date expiresAt;

    @Getter @Setter
    private Long userId;

    @Getter @Setter
    private Long tenantId;

    @Getter @Setter
    private String roles[];

}
