package com.educative.auth;

import net.jodah.expiringmap.ExpiringMap;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RevocationServiceImpl implements RevocationService {
    private static final int duration = 15; // 15 minutes

    // use self expiring maps
    private Map<String, String> tokenRevocations;
    private Map<Long, Long> userRevocations;
    private Map<Long, Long> tenantRevocations;

    public RevocationServiceImpl() {
        tokenRevocations = ExpiringMap.builder()
                .expiration(duration, TimeUnit.MINUTES)
                .build();
        userRevocations = ExpiringMap.builder()
                .expiration(duration, TimeUnit.MINUTES)
                .build();
        tenantRevocations = ExpiringMap.builder()
                .expiration(duration, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public void revokeToken(String tokenId) {
        tokenRevocations.put(tokenId, tokenId);
    }

    @Override
    public void revokeUser(Long userId) {
        userRevocations.put(userId, userId);
    }

    @Override
    public void revokeTenant(Long tenantId) {
        tenantRevocations.put(tenantId, tenantId);
    }

    @Override
    public boolean isTokenRevoked(String tokenId) {
        return tokenRevocations.containsKey(tokenId);
    }

    @Override
    public boolean isUserRevoked(Long userId) {
        return userRevocations.containsKey(userId);
    }

    @Override
    public boolean isTenantRevoked(Long tenantId) {
        return tenantRevocations.containsKey(tenantId);
    }

    @Override
    public void reinstateUser(Long userId) {
        userRevocations.remove(userId);
    }

    @Override
    public void reinstateTenant(Long tenantId) {
        tenantRevocations.remove(tenantId);
    }

}
