package com.educative.auth;

import com.educative.commons.exception.JWTKeyErrorCode;
import com.educative.commons.exception.ResourceExpiredException;
import com.educative.commons.exception.ResourceNotFoundException;
import com.educative.jwt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;

public class JWTKeyClient {

    private static final Logger _logger = LoggerFactory.getLogger(JWTKeyClient.class.getName());

    private String baseURL;

    @Autowired
    private KeyHelper keyHelper;

    @Autowired
    private KeyService keyService;

    @Autowired
    private KeyMapper keyMapper;

    /**
     * Create key pair. Keep the private key on your application, which is generated only once.
     *
     * @throws JWTKeyClientException timeout or errors during establishing the connection
     */
    public KeyWithPrivateKeyResponse createKeyPair(
            String issuer, Integer timeToLive, String description) throws JWTKeyClientException {
        KeyRequest keyRequest = new KeyRequest(issuer, timeToLive, description);
       return createKey(keyRequest);
    }

    public KeyWithPrivateKeyResponse createKeyPair(String issuer) throws JWTKeyClientException {
        KeyRequest keyRequest = new KeyRequest(issuer);
        return createKey(keyRequest);
    }

    private KeyWithPrivateKeyResponse createKey(@Validated @RequestBody KeyRequest request) {
        Key k = new Key();
        try {
            KeyPair keyPair = keyHelper.generateKeyPair();
            k.setContent(keyHelper.keyToString(keyPair.getPublic(), "RSA PUBLIC KEY"));
            k.setPrivateKey(keyHelper.keyToString(keyPair.getPrivate(), "RSA PRIVATE KEY"));
        } catch (NoSuchAlgorithmException | NoSuchProviderException | IOException e) {
            _logger.error(e.getMessage());
            throw new RuntimeException("COULD_NOT_GENERATE_KEY");
        }

        Key key = keyService.save(k);
        KeyWithPrivateKeyResponse response = KeyMapper.INSTANCE.keyToKeyWithPrivateKeyResponse(key);
        return response;
    }

    /**
     * Fetch a public key.
     *
     * @throws JWTKeyClientException timeout or errors during establishing the connection
     */
    public KeyResponse fetchPublicKeyByID(String id) throws JWTKeyClientException {
        Key key = keyService.get(Long.parseLong(id));
        if (key == null) {
            _logger.error("key with id = {} not found", id);
            throw new ResourceNotFoundException(JWTKeyErrorCode.KEY_NOT_FOUND);
        }

        Date expiredAt = key.getExpiresAt();
        if (expiredAt != null && expiredAt.before(new Date())) {
            _logger.error("key with id = {} is expired", id);
            throw new ResourceExpiredException(JWTKeyErrorCode.KEY_EXPIRED);
        }

        KeyResponse response = KeyMapper.INSTANCE.keyToKeyResponse(key);

        return response;
    }

}
