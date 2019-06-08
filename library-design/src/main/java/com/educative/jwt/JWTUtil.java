package com.educative.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.educative.auth.JWTSpec;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

public class JWTUtil {

    private static final Logger _logger = LoggerFactory.getLogger(JWTUtil.class);

    public static final String JWT_COOKIE_NAME = "jwt";
    public static final String TENANTID_CLAIM = "tenantId";
    public static final String ROLEZ_CLAIM = "rolez";
    public static final String CSRF_TOKEN_CLAIM = "csrfToken";
    public static final String CSRF_TOKEN_HEADER = "X-CSRF-TOKEN";
    public static final String INTEGRATION_TYEP_CLAIM = "integrationType";
    public static final String AUTH_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

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

    public static Long getUserId(DecodedJWT jwt) {
        try {
            return Long.valueOf(jwt.getSubject());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String extractJWTToken(HttpServletRequest request) {
        String token = extractJWTTokenFromHeader(request);
        if (token == null) {
            token = extractJWTTokenFromCookie(request);
        }
        return token;
    }

    private static String extractJWTTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER);
        if (StringUtils.isBlank(authHeader) || !authHeader.startsWith(BEARER_PREFIX)) {
            return null;
        }

        return authHeader.substring(BEARER_PREFIX.length());
    }

    private static String extractJWTTokenFromCookie(HttpServletRequest request) {
        Cookie cookies[] = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (JWTUtil.JWT_COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public static Long getTenantId(DecodedJWT jwt) {
        try {
            return Long.valueOf(jwt.getClaim(TENANTID_CLAIM).asString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String getCsrfToken(DecodedJWT jwt) {
        Claim claim = jwt.getClaim(CSRF_TOKEN_CLAIM);
        return (claim != null)? claim.asString() : null;
    }

    /**
     * Decode jwt token
     *
     * @param token with jwt format as string.
     * @return a decoded JWT.
     * @throws JWTDecodeException if any part of the token contained an invalid jwt or JSON
     *                            format of each of the jwt parts.
     */
    public static DecodedJWT decodeToken(String token) {
        return JWT.decode(token);
    }

    /**
     * Perform the verification against the given Token
     *
     * @param token to verify.
     * @return a verified and decoded JWT.
     * @throws AlgorithmMismatchException     if the algorithm stated in the token's header it's
     *                                        not equal to the one defined in the {@link JWTVerifier}.
     * @throws SignatureVerificationException if the signature is invalid.
     * @throws TokenExpiredException          if the token has expired.
     * @throws InvalidClaimException          if a claim contained a different value than the expected one.
     */
    public static DecodedJWT verifyToken(PublicKey publicKey, String token) {
        Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicKey, null);
        JWTVerifier verifier = JWT.require(algorithm).build(); // Reusable verifier instance
        return verifier.verify(token);
    }


    /**
     * Convert string public key to java.security Public Key
     *
     * @param publicKey public key string
     * @return PublicKey object or null
     */
    public static PublicKey extractPublicKey(String publicKey) {
        String publicKeyContent = publicKey
                .replaceAll("\\n", "")
                .replace("-----BEGIN RSA PUBLIC KEY-----", "")
                .replace("-----END RSA PUBLIC KEY-----", "");

        KeyFactory kf;
        try {
            kf = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            _logger.error(e.getMessage(), e);
            return null;
        }

        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));

        try {
            return kf.generatePublic(keySpecX509);
        } catch (InvalidKeySpecException e) {
            _logger.error(e.getMessage(), e);
            return null;
        }
    }


}
