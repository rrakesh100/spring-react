package com.educative.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.educative.jwt.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class JWTAuthenticationProvider implements AuthenticationProvider {

    private static final Logger _logger = LoggerFactory.getLogger(JWTAuthenticationProvider.class);

    private JWTTokenService jwtTokenService;

    private RevocationService revocationService;

    public void setJWTTokenService(JWTTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    public void setRevocationService(RevocationService revocationService) {
        this.revocationService = revocationService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JWTAuthenticationToken token = (JWTAuthenticationToken) authentication;
        DecodedJWT jwt = null;

        try {
            jwt = jwtTokenService.verifyToken(token.getJWTToken());
            Long tenantId = JWTUtil.getTenantId(jwt);
            Long userId = JWTUtil.getUserId(jwt);
            if(tenantId == null) {
                _logger.debug("Missing tenantId Claim");
                throw new BadCredentialsException("Invalid JWT Token: Missing tenantId Claim");
            }
            if(userId == null) {
                _logger.debug("Missing or invalid subject Claim");
                throw new BadCredentialsException("Invalid JWT Token: Missing or invalid subject Claim");
            }
            if(revocationService != null) {
                if(revocationService.isTokenRevoked(jwt.getId())) {
                    _logger.info("token has been revoked: {}", jwt.getId());
                    throw new BadCredentialsException("Invalid JWT Token: Revoked");
                }
                if(revocationService.isTenantRevoked(tenantId)) {
                    _logger.info("tenant has been deleted/disabled: {}", tenantId);
                    throw new BadCredentialsException("Invalid JWT Token: Tenant revoked");
                }
                if(revocationService.isUserRevoked(userId)) {
                    _logger.info("user has been deleted/disabled: {}", userId);
                    throw new BadCredentialsException("Invalid JWT Token: User revoked");
                }
            }
        } catch (Exception e) {
            _logger.trace(e.getMessage());
            throw new BadCredentialsException("Invalid JWT Token: " + e.getMessage());
        }

        return new JWTAuthenticationToken(token.getJWTToken(), new JWTUserDetails(jwt));

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JWTAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
