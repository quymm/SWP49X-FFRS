package com.controller;

import com.dto.InputTimeEnableDTO;
import com.services.TimeEnableServices;
import com.services.TimeSlotServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class TimeEnableController {

    @Autowired
    TimeEnableServices timeEnableServices;

    @Autowired
    TimeSlotServices timeSlotServices;

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "enableTime/setUpTimeEnable", method = RequestMethod.POST)
    public ResponseEntity setUpTimeEnable(@RequestBody InputTimeEnableDTO inputTimeEnableDTO){
        return new ResponseEntity(timeEnableServices.setUpTimeEnable(inputTimeEnableDTO), HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "enableTime/findFreeTime", method = RequestMethod.GET)
    public ResponseEntity getFreeTimeWithFieldIdAndDate(@RequestParam("fieldId") int fieldId, @RequestParam("targetDate") Date targetDate){
        return new ResponseEntity(timeSlotServices.findTimeSlotByDateFieldIdAndReservateStatus(targetDate, fieldId, false), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "enableTime/getTimeEnableByFieldOwnerId", method = RequestMethod.GET)
    public ResponseEntity getTimeEnableByFieldOwnerId(@RequestParam("fieldOwnerId") int fieldOwnerId){
        return new ResponseEntity(timeEnableServices.findTimeEnableByFieldOwnerId(fieldOwnerId), HttpStatus.OK);
    }

}
