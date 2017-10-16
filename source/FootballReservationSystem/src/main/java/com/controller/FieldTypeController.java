package com.controller;

import com.dto.InputFieldTypeDTO;
import com.dto.Wrapper;
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

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/field-type/managed-field-type", method = RequestMethod.POST)
    public ResponseEntity createNewFieldType(@RequestBody InputFieldTypeDTO inputFieldTypeDTO){
        Wrapper wrapper = new Wrapper(fieldTypeServices.createNewFieldType(inputFieldTypeDTO), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/field-type/managed-field-type", method = RequestMethod.GET)
    public ResponseEntity getFieldTypeById(@RequestParam("field-type-id") int fieldTypeId){
        Wrapper wrapper = new Wrapper(fieldTypeServices.findById(fieldTypeId), HttpStatus.OK.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/field-type/managed-field-type", method = RequestMethod.DELETE)
    public ResponseEntity delteFieldType(@RequestParam("field-type-id") int fieldTypeId){
        Wrapper wrapper = new Wrapper(fieldTypeServices.deleteFieldType(fieldTypeId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }
}
