package com.educative.jwt;

import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public interface KeyHelper {

    public KeyPair generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException;

    String keyToString(Key key, String description) throws IOException;
}
