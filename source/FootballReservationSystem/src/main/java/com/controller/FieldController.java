package com.controller;

import com.dto.InputFieldDTO;
import com.dto.Wrapper;
import com.services.FieldServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by MinhQuy on 9/24/2017.
 */
@RestController
public class FieldController {
    @Autowired
    FieldServices fieldServices;

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/field/managed-field", method = RequestMethod.POST)
    public ResponseEntity createNewField(@RequestBody InputFieldDTO inputFieldDTO) {
        Wrapper wrapper = new Wrapper(fieldServices.createNewField(inputFieldDTO), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/field/field-owner", method = RequestMethod.GET)
    public ResponseEntity getFieldByFieldOwnerId(@RequestParam("field-owner-id") int fieldOwnerId) {
        Wrapper wrapper = new Wrapper(fieldServices.findFieldEntityByFieldOwnerId(fieldOwnerId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/field/managed-field", method = RequestMethod.GET)
    public ResponseEntity getFieldById(@RequestParam("field-id") int fieldId) {
        Wrapper wrapper = new Wrapper(fieldServices.findFieldEntityById(fieldId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/field/managed-field", method = RequestMethod.DELETE)
    public ResponseEntity disableField(@RequestParam("field-id") int fieldId, @RequestParam("date-from") String dateFrom, @RequestParam("date-to") String dateTo) {
        Wrapper wrapper = new Wrapper(fieldServices.disableField(fieldId, dateFrom, dateTo), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

}
