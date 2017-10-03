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
    @RequestMapping(value = "enableTime/setUpTimeEnable", method = RequestMethod.POST)
    public ResponseEntity setUpTimeEnable(@RequestBody InputTimeEnableDTO inputTimeEnableDTO){
        return new ResponseEntity(timeEnableServices.setUpTimeEnable(inputTimeEnableDTO), HttpStatus.CREATED);
    }
<<<<<<< HEAD
=======

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

>>>>>>> master
}
