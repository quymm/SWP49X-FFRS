package com.controller;

import com.dto.InputFieldDTO;
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
        FieldEntity fieldEntity = fieldServices.createNewField(inputFieldDTO);
        return new ResponseEntity(fieldEntity, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/field/field-owner", method = RequestMethod.GET)
    public ResponseEntity getFieldByFieldOwnerId(@RequestParam("field-owner-id") int fieldOwnerId){
        List<FieldEntity> fieldEntityList = fieldServices.findFieldEntityByFieldOwnerId(fieldOwnerId);
        return new ResponseEntity(fieldEntityList, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/field/field-owner-and-name", method = RequestMethod.GET)
    public ResponseEntity getFieldByFieldNameAndFieldOwnerId(@RequestParam("field-owner-id") int fieldOwnerId,
                                                             @RequestParam("field-name") String fieldName){
        return new ResponseEntity(fieldServices.findFieldEntityByFieldNameAndFieldOwnerId(fieldName, fieldOwnerId), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/field/managed-field", method = RequestMethod.GET)
    public ResponseEntity getFieldById(@RequestParam("field-id") int fieldId){
        return new ResponseEntity(fieldServices.findFieldEntityById(fieldId), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/field/managed-field", method = RequestMethod.DELETE)
    public ResponseEntity deleteField(@RequestParam("field-id") int fieldId){
        return new ResponseEntity(fieldServices.deleteFieldEntity(fieldId), HttpStatus.OK);
    }

}
