package com.educative.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
public class JWTSpec {

    @Getter @Setter
    private String id;
    @Getter @Setter
    private String subject;
    @Getter @Setter
    private String tenantId;
    @Getter @Setter
    private String rolez[];

    @Getter @Setter
    private Date issuedAt;


    @Getter @Setter
    private Date expiresAt;
    @Getter @Setter
    private String issuer;
    @Getter @Setter
    private String audience;
    @Getter @Setter
    private String integrationType;
    @Getter @Setter
    private String csrfToken;


}
