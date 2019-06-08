package com.educative.auth;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.educative.commons.exception.JWTTokenException;
import com.educative.jwt.JWTKeyClientException;
import com.educative.jwt.JWTUtil;
import com.educative.jwt.KeyResponse;
import com.educative.jwt.KeyWithPrivateKeyResponse;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class JWTTokenServiceImpl implements JWTTokenService {

    private static final Logger _logger = LoggerFactory.getLogger(JWTTokenServiceImpl.class);

    private Long privateKeyIdInCache;
    private PrivateKey privateKeyInCache;
    private JWTKeyClient client;


    // give 15 seconds leeway otherwise the token verifier may fail to verify due to timing issue
    private static final int ISSUED_AT_LEEWAY = 15;



    public JWTTokenServiceImpl(JWTKeyClient client) {

    }

    /**
     * Cache loader for retrieving public key
     */
    private CacheLoader<String, PublicKey> publicKeyCacheLoader = new CacheLoader<String, PublicKey>() {
        @Override
        public PublicKey load(String keyId) throws JWTKeyClientException {
            KeyResponse response = client.fetchPublicKeyByID(keyId);
            return JWTUtil.extractPublicKey(response.getContent());
        }
    };

    /**
     * public key cache
     */
    private LoadingCache<String, PublicKey> publicKeyCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .build(publicKeyCacheLoader);

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
    public DecodedJWT verifyToken(String token) throws JWTTokenException {
        DecodedJWT jwt;
        try {
            jwt = JWTUtil.decodeToken(token);
        } catch (JWTVerificationException e) {
            _logger.error("could not decode jwt token: {}", token);
            throw new JWTTokenException(e.getMessage(), e);
        }

        String kid = jwt.getKeyId();
        if (kid == null || kid.length() <= 0) {
            _logger.error("kid must not be null: JWT {}", jwt.getToken());
            throw new JWTTokenException("kid must not be null");
        }

        PublicKey key;
        try {
            key = publicKeyCache.get(kid);
        } catch (ExecutionException e) {
            _logger.error("could not find public key: kid {}", kid);
            throw new JWTTokenException(e.getMessage());
        }

        try {
            return JWTUtil.verifyToken(key, token);
        } catch (TokenExpiredException e) {
            _logger.error("TokenExpiredException: {}", e.getMessage());
            throw new JWTTokenException(e.getMessage(), e);
        } catch (JWTVerificationException e) {
            // show JWT token contents and public key used to verify the token
            _logger.error("failed to verify jwt token - subject: {}, issuer: {}, kid: {}, token: {} using public key: {}",
                    jwt.getSubject(), jwt.getIssuer(), jwt.getKeyId(), token, Base64.getEncoder().encodeToString(key.getEncoded())
            );

            _logger.error(e.getMessage(), e);
            throw new JWTTokenException(e.getMessage(), e);
        }
    }
}
