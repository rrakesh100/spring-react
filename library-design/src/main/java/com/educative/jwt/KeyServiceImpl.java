package com.educative.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KeyServiceImpl implements KeyService {


    @Autowired
    KeyRepository keyRepository;

    @Override
    public Key save(Key key) {
           return keyRepository.save(key);
    }

    @Override
    @Transactional(readOnly = true)
    public Key get(Long id) {
        return keyRepository.findById(id).get();
    }

    @Override
    public void delete(Long id) {
        keyRepository.deleteById(id);
    }
}
