package com.educative.jwt;

import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-06-21T16:21:05+0530",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"
)
public class KeyMapperImpl implements KeyMapper {

    @Override
    public KeyResponse keyToKeyResponse(Key key) {
        if ( key == null ) {
            return null;
        }

        KeyResponse keyResponse = new KeyResponse();

        keyResponse.setId( key.getId() );
        keyResponse.setIssuer( key.getIssuer() );
        keyResponse.setContent( key.getContent() );
        keyResponse.setDescription( key.getDescription() );
        keyResponse.setExpiresAt( key.getExpiresAt() );
        keyResponse.setCreated( key.getCreated() );

        return keyResponse;
    }

    @Override
    public KeyWithPrivateKeyResponse keyToKeyWithPrivateKeyResponse(Key key) {
        if ( key == null ) {
            return null;
        }

        KeyWithPrivateKeyResponse keyWithPrivateKeyResponse = new KeyWithPrivateKeyResponse();

        keyWithPrivateKeyResponse.setId( key.getId() );
        keyWithPrivateKeyResponse.setIssuer( key.getIssuer() );
        keyWithPrivateKeyResponse.setContent( key.getContent() );
        keyWithPrivateKeyResponse.setDescription( key.getDescription() );
        keyWithPrivateKeyResponse.setExpiresAt( key.getExpiresAt() );
        keyWithPrivateKeyResponse.setCreated( key.getCreated() );
        keyWithPrivateKeyResponse.setPrivateKey( key.getPrivateKey() );

        return keyWithPrivateKeyResponse;
    }
}
