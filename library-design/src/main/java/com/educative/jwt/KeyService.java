package com.educative.jwt;

public interface KeyService {

    public Key save(Key key);

    public Key get(Long id);

    public void delete(Long id);


}
