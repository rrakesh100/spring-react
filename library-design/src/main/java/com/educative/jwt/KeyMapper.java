package com.educative.jwt;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Date;

@Mapper
public interface KeyMapper {

    KeyMapper INSTANCE = Mappers.getMapper(KeyMapper.class);

    KeyResponse keyToKeyResponse(Key key);

    KeyWithPrivateKeyResponse keyToKeyWithPrivateKeyResponse(Key key);

    default Key keyRequestToKey(KeyRequest keyRequest) {
        Key key = new Key();
        key.setIssuer(keyRequest.getIssuer());
        key.setDescription(keyRequest.getDescription());
        Integer timeToLive = keyRequest.getTimeToLive();

        if (timeToLive != null && timeToLive > 0) {
            Date expiresAt = new Date();
            expiresAt.setTime(expiresAt.getTime() + timeToLive);
            key.setExpiresAt(expiresAt);
        }
        return key;
    }
}
