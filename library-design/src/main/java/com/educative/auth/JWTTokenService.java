package com.educative.auth;

import com.auth0.jwt.interfaces.DecodedJWT;

public interface JWTTokenService {

    public String createToken(JWTSpec spec);

    public DecodedJWT verifyToken(String token);
}
