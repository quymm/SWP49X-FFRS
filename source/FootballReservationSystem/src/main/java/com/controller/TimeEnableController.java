package com.controller;

import com.dto.InputTimeEnableDTO;
import com.services.TimeEnableServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TimeEnableController {

    @Autowired
    TimeEnableServices timeEnableServices;

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "enableTime/setUpTimeEnable", method = RequestMethod.POST)
    public ResponseEntity setUpTimeEnable(@RequestBody InputTimeEnableDTO inputTimeEnableDTO){
        return new ResponseEntity(timeEnableServices.setUpTimeEnable(inputTimeEnableDTO), HttpStatus.CREATED);
    }
}
