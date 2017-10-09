package com.controller;

import com.dto.InputReservationDTO;
import com.services.TimeSlotServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author MinhQuy
 */
@RestController
public class TimeSlotController {
    @Autowired
    TimeSlotServices timeSlotServices;

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/match/free-time", method = RequestMethod.GET)
    public ResponseEntity getFreeTime(@RequestParam("field-owner-id") int fieldOwnerId, @RequestParam("field-type-id") int fieldTypeId,
                                      @RequestParam("date") String date){
        return new ResponseEntity(timeSlotServices.findFreeTimeByFieldOwnerTypeAndDate(fieldOwnerId, fieldTypeId, date), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/match/friendly-match", method = RequestMethod.POST)
    public ResponseEntity reserveFriendMatch(@RequestBody InputReservationDTO inputReservationDTO){
        return new ResponseEntity(timeSlotServices.reserveFriendlyMatch(inputReservationDTO), HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/match/match-upcoming", method = RequestMethod.GET)
    public ResponseEntity getMatchUpComing(@RequestParam("field-owner-id") int fieldOwnerId, @RequestParam("field-type-id") int fieldTypeId,
                                           @RequestParam("date") String date){
        return new ResponseEntity(timeSlotServices.findUpcomingReservationByDate(date, fieldOwnerId, fieldTypeId), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/match/free-field", method = RequestMethod.GET)
    public ResponseEntity getFreeField(@RequestParam("field-owner-id") int fieldOwnerId, @RequestParam("field-type-id") int fieldTypeId,
                                          @RequestParam("date") String targetDate, @RequestParam("time") String targetTime){
        return new ResponseEntity(timeSlotServices.getListFreeFieldAtSpecificTime(targetDate, targetTime, fieldOwnerId, fieldTypeId), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/match/set-field", method = RequestMethod.PUT)
    public ResponseEntity setFieldForMatch(@RequestParam("time-slot-id") int timeSlotId, @RequestParam("field-id") int fieldId){
        return new ResponseEntity(timeSlotServices.setTimeForTimeSlot(timeSlotId, fieldId), HttpStatus.OK);
    }


}
