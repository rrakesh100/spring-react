package com.educative.jwt;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class KeyRequest {

    @NotNull
    @Getter @Setter
    private String Issuer;

    @Getter @Setter
    private Integer TimeToLive;

    @Getter @Setter
    private String Description;

    public KeyRequest() {
    }

    public KeyRequest(String issuer) {
        this.Issuer=issuer;
    }

    public KeyRequest(String issuer, Integer timeToLive, String description) {
        Issuer = issuer;
        TimeToLive = timeToLive;
        Description = description;
    }
}
