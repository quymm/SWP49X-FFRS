package com.controller;

import com.dto.InputFieldTypeDTO;
import com.entity.FieldTypeEntity;
import com.services.FieldTypeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by MinhQuy on 9/24/2017.
 */
@RestController
public class FieldTypeController {
    @Autowired
    FieldTypeServices fieldTypeServices;

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/fieldType/createNewFieldType", method = RequestMethod.POST)
    public ResponseEntity createNewFieldType(@RequestBody InputFieldTypeDTO inputFieldTypeDTO){
        FieldTypeEntity fieldTypeEntity = fieldTypeServices.createNewFieldTypeEntity(inputFieldTypeDTO);
        return new ResponseEntity(fieldTypeEntity, HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/fieldType/getByFieldTypeId", method = RequestMethod.GET)
    public ResponseEntity getFieldTypeById(@RequestParam("fieldTypeId") int fieldTypeId){
        FieldTypeEntity fieldTypeEntity = fieldTypeServices.findFieldTypeEntityById(fieldTypeId);
        return new ResponseEntity(fieldTypeEntity, HttpStatus.OK);
    }
}
