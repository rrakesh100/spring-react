package com.educative.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.educative.auth.JWTSpec;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

public class JWTUtil {

    private static final Logger _logger = LoggerFactory.getLogger(JWTUtil.class);

    public static final String TENANTID_CLAIM = "tenantId";
    public static final String ROLEZ_CLAIM = "rolez";
    public static final String CSRF_TOKEN_CLAIM = "csrfToken";
    public static final String CSRF_TOKEN_HEADER = "X-CSRF-TOKEN";
    public static final String INTEGRATION_TYEP_CLAIM = "integrationType";


    public static PrivateKey extractPrivateKey(String privateKey) {
        String privateKeyContent = privateKey
                .replaceAll("\\n", "")
                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "");

        KeyFactory kf;
        try {
            kf = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            _logger.error(e.getMessage(), e);
            return null;
        }

        PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
        try {
            return kf.generatePrivate(keySpecPKCS8);
        } catch (InvalidKeySpecException e) {
            _logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static String createToken(
            PrivateKey privateKey, Long keyID, JWTSpec spec)
            throws JWTCreationException {

        Algorithm algorithm = Algorithm.RSA256(null, (RSAPrivateKey) privateKey);
        String id = spec.getId();
        if(StringUtils.isBlank(id)) {
            id = UUID.randomUUID().toString();
        }

        JWTCreator.Builder builder = JWT.create()
                .withJWTId(id)
                .withKeyId(keyID.toString())
                .withIssuer(spec.getIssuer())
                .withAudience(spec.getAudience())
                .withSubject(spec.getSubject())
                .withIssuedAt(spec.getIssuedAt())
                .withExpiresAt(spec.getExpiresAt())
                .withClaim(TENANTID_CLAIM, spec.getTenantId())
                .withArrayClaim(ROLEZ_CLAIM, spec.getRolez())
                .withClaim(CSRF_TOKEN_CLAIM, spec.getCsrfToken())
                .withClaim(INTEGRATION_TYEP_CLAIM, spec.getIntegrationType());

        return builder.sign(algorithm);
    }

}
