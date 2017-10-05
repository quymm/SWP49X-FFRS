package com.controller;

import com.dto.InputTimeEnableDTO;
import com.services.TimeEnableServices;
<<<<<<< HEAD
=======
import com.services.TimeSlotServices;
>>>>>>> master
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

<<<<<<< HEAD
=======
import java.util.Date;

>>>>>>> master
@RestController
public class TimeEnableController {

    @Autowired
    TimeEnableServices timeEnableServices;

<<<<<<< HEAD
=======
    @Autowired
    TimeSlotServices timeSlotServices;

>>>>>>> master
    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/time-enable/managed-time-enable", method = RequestMethod.POST)
    public ResponseEntity setUpTimeEnable(@RequestBody InputTimeEnableDTO inputTimeEnableDTO){
        return new ResponseEntity(timeEnableServices.setUpTimeEnable(inputTimeEnableDTO), HttpStatus.CREATED);
    }
<<<<<<< HEAD
=======

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/time-enable/field-owner-and-type", method = RequestMethod.GET)
    public ResponseEntity getTimeEnableByFieldOwnerIdAndFieldTypeId(@RequestParam("field-owner-id") int fieldOwnerId,
                                                                    @RequestParam("field-type-id") int fieldTypeId){
        return new ResponseEntity(timeEnableServices.findTimeEnableByFieldOwnerIdAndFieldTypeId(fieldOwnerId, fieldTypeId), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/time-enable/managed-time-enable", method = RequestMethod.GET)
    public ResponseEntity getTimeEnableByFieldOwnerId(@RequestParam("field-owner-id") int fieldOwnerId){
        return new ResponseEntity(timeEnableServices.findTimeEnableByFieldOwnerId(fieldOwnerId), HttpStatus.OK);
    }

>>>>>>> master
}
