package com.educative.auth;

public interface RevocationService {

    void revokeToken(String tokenId);

    void revokeUser(Long userId);

    void revokeTenant(Long tenantId);

    boolean isTokenRevoked(String tokenId);

    boolean isUserRevoked(Long userId);

    boolean isTenantRevoked(Long tenantId);

    void reinstateUser(Long userId);

    void reinstateTenant(Long tenantId);
}
