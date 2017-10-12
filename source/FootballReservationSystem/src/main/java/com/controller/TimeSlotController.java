package com.controller;

import com.dto.InputMatchingRequestDTO;
import com.dto.InputReservationDTO;
import com.services.MatchServices;
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

    @Autowired
    MatchServices matchServices;

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/match/free-time", method = RequestMethod.GET)
    public ResponseEntity getFreeTime(@RequestParam("field-owner-id") int fieldOwnerId, @RequestParam("field-type-id") int fieldTypeId,
                                      @RequestParam("date") String date){
        return new ResponseEntity(timeSlotServices.findFreeTimeByFieldOwnerTypeAndDate(fieldOwnerId, fieldTypeId, date), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/match/reserve-time-slot", method = RequestMethod.POST)
    public ResponseEntity reserveTimeSlot(@RequestBody InputReservationDTO inputReservationDTO){
        return new ResponseEntity(timeSlotServices.reserveTimeSlot(inputReservationDTO), HttpStatus.CREATED);
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

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/match/friendly-match", method = RequestMethod.POST)
    public ResponseEntity reserveFriendlyMatch(@RequestParam("time-slot-id") int timeSlotId, @RequestParam("user-id") int userId){
        return new ResponseEntity(matchServices.reserveFriendlyMatch(timeSlotId, userId), HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/match/matching-request", method = RequestMethod.POST)
    public ResponseEntity createNewMatchingRequest(@RequestBody InputMatchingRequestDTO inputMatchingRequestDTO){
        return new ResponseEntity(matchServices.createNewMatchingRequest(inputMatchingRequestDTO), HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/match/matching-request", method = RequestMethod.GET)
    public ResponseEntity suggestOpponent(@RequestParam("user-id") int userId, @RequestParam("field-type-id") int fieldTypeId,
                                          @RequestParam("longitute") String longitute, @RequestParam("latitute") String latitute,
                                          @RequestParam("date") String date, @RequestParam("start-time") String startTime){
        return new ResponseEntity(matchServices.suggestOpponent(userId, fieldTypeId, longitute, latitute, date, startTime), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/match/choose-field", method = RequestMethod.POST)
    public ResponseEntity chooseSuitableField(@RequestBody InputMatchingRequestDTO inputMatchingRequestDTO, @RequestParam("matching-request-id") int matchingRequestId){
        return new ResponseEntity(matchServices.chooseSuitableField(inputMatchingRequestDTO, matchingRequestId), HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/match/tour-match", method = RequestMethod.POST)
    public ResponseEntity reserveTourMatch(@RequestParam("time-slot-id") int timeSlotId, @RequestParam("user-id") int userId, @RequestParam("opponent-id") int opponentId){
        return new ResponseEntity(matchServices.reserveTourMatch(timeSlotId, userId, opponentId), HttpStatus.CREATED);
    }


}
