package com.controller;

import com.dto.InputTimeEnableDTO;
import com.dto.Wrapper;
import com.services.TimeEnableServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEnableController {

    @Autowired
    TimeEnableServices timeEnableServices;

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/time-enable/managed-time-enable", method = RequestMethod.POST)
    public ResponseEntity setUpTimeEnable(@RequestBody List<InputTimeEnableDTO> inputTimeEnableDTOList){
        Wrapper wrapper = new Wrapper(timeEnableServices.setUpTimeEnable(inputTimeEnableDTOList), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/time-enable/field-owner-and-type", method = RequestMethod.GET)
    public ResponseEntity getTimeEnableByFieldOwnerIdAndFieldTypeId(@RequestParam("field-owner-id") int fieldOwnerId,
                                                                    @RequestParam("field-type-id") int fieldTypeId){
        Wrapper wrapper = new Wrapper(timeEnableServices.findTimeEnableByFieldOwnerIdAndFieldTypeId(fieldOwnerId, fieldTypeId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/time-enable/managed-time-enable", method = RequestMethod.GET)
    public ResponseEntity getTimeEnableByFieldOwnerId(@RequestParam("field-owner-id") int fieldOwnerId){
        Wrapper wrapper = new Wrapper(timeEnableServices.findTimeEnableByFieldOwnerId(fieldOwnerId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

}
