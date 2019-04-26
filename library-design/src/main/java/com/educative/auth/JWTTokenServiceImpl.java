package com.educative.auth;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.educative.jwt.JWTKeyClientException;
import com.educative.jwt.JWTUtil;
import com.educative.jwt.KeyWithPrivateKeyResponse;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.util.Date;

public class JWTTokenServiceImpl implements JWTTokenService {

    private static final Logger _logger = LoggerFactory.getLogger(JWTTokenServiceImpl.class);

    private Long privateKeyIdInCache;
    private PrivateKey privateKeyInCache;
    private JWTKeyClient client;


    // give 15 seconds leeway otherwise the token verifier may fail to verify due to timing issue
    private static final int ISSUED_AT_LEEWAY = 15;



    public JWTTokenServiceImpl(JWTKeyClient client) {

    }




    @Override
    public String createToken(JWTSpec spec) {
        if (privateKeyIdInCache == null || privateKeyInCache == null) {
            KeyWithPrivateKeyResponse keyWithPrivateKeyResponse = null;
            try {
                keyWithPrivateKeyResponse = client.createKeyPair(spec.getIssuer());
            } catch (JWTKeyClientException e) {
                _logger.error("Error occured", e);
            }

            privateKeyIdInCache = keyWithPrivateKeyResponse.getId();
            privateKeyInCache = JWTUtil.extractPrivateKey(keyWithPrivateKeyResponse.getPrivateKey());
        }

        try {
            // set issueAt 15 seconds earlier
            Date issuedAt = DateUtils.addSeconds(spec.getIssuedAt(), -ISSUED_AT_LEEWAY);
            spec.setIssuedAt(issuedAt);
            return JWTUtil.createToken(
                    privateKeyInCache , privateKeyIdInCache, spec);
        } catch (JWTCreationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public DecodedJWT verifyToken(String token) {
        return null;
    }
}
