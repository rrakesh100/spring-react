package com.educative.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.educative.commons.exception.JWTTokenException;

public interface JWTTokenService {

    public String createToken(JWTSpec spec);

    public DecodedJWT verifyToken(String token) throws JWTTokenException;
}
