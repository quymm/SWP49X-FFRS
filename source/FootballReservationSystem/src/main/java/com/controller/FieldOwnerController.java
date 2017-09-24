package com.controller;

import com.dto.InputFieldOwnerDTO;
import com.entity.FieldOwnerEntity;
import com.services.FieldOwnerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by MinhQuy on 9/24/2017.
 */
@RestController
public class FieldOwnerController {
    @Autowired
    FieldOwnerServices fieldOwnerServices;

    @RequestMapping(value = "/fieldOwner/createNewFieldOwner", method = RequestMethod.POST)
    public ResponseEntity createNewFieldOwner(@RequestBody InputFieldOwnerDTO inputFieldOwnerDTO) {
        FieldOwnerEntity fieldOwnerEntity = fieldOwnerServices.createNewFieldOwner(inputFieldOwnerDTO);
        return new ResponseEntity(fieldOwnerEntity, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/fieldOwner/getByFieldOwnerId", method = RequestMethod.GET)
    public ResponseEntity getFieldOwnerById(@RequestParam("fieldOwnerId") int fieldOwnerId){
        FieldOwnerEntity fieldOwnerEntity = fieldOwnerServices.getFieldOwnerEntityByFieldOwnerId(fieldOwnerId);
        return new ResponseEntity(fieldOwnerEntity, HttpStatus.FOUND);
    }
}
