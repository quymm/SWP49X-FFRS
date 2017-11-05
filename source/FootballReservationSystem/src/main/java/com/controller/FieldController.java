package com.controller;

import com.dto.InputFieldDTO;
import com.dto.Wrapper;
import com.entity.FieldEntity;
import com.services.FieldServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by MinhQuy on 9/24/2017.
 */
@RestController
public class FieldController {
    @Autowired
    FieldServices fieldServices;

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/field/managed-field", method = RequestMethod.POST)
    public ResponseEntity createNewField(@RequestBody InputFieldDTO inputFieldDTO){
        Wrapper wrapper = new Wrapper(fieldServices.createNewField(inputFieldDTO), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/field/field-owner", method = RequestMethod.GET)
    public ResponseEntity getFieldByFieldOwnerId(@RequestParam("field-owner-id") int fieldOwnerId){
        Wrapper wrapper = new Wrapper(fieldServices.findFieldEntityByFieldOwnerId(fieldOwnerId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/field/field-owner-and-name", method = RequestMethod.GET)
    public ResponseEntity getFieldByFieldNameAndFieldOwnerId(@RequestParam("field-owner-id") int fieldOwnerId,
                                                             @RequestParam("field-name") String fieldName){
        Wrapper wrapper = new Wrapper(fieldServices.findFieldEntityByFieldNameAndFieldOwnerId(fieldName, fieldOwnerId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/field/managed-field", method = RequestMethod.GET)
    public ResponseEntity getFieldById(@RequestParam("field-id") int fieldId){
        Wrapper wrapper = new Wrapper(fieldServices.findFieldEntityById(fieldId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/field/managed-field", method = RequestMethod.DELETE)
    public ResponseEntity deleteField(@RequestParam("field-id") int fieldId){
        Wrapper wrapper = new Wrapper(fieldServices.deleteFieldEntity(fieldId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

}
