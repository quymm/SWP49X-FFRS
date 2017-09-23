package com.services;

import com.entity.FieldOwnerEntity;
import com.repository.FieldOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by truonghuuthanh on 9/23/17.
 */
@Service
public class FieldOwnerService {

    @Autowired
    FieldOwnerRepository fieldOwnerRepository;

    public FieldOwnerEntity fieldOwnerLogin(String username, String password){
        return fieldOwnerRepository.findByUsernameAndPassword(username, password);
    }
}
