package com.controller;

import com.dto.InputMatchingRequestDTO;
import com.dto.InputReservationDTO;
import com.dto.Wrapper;
import com.services.MatchServices;
import com.services.TimeSlotServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.image.WritableRaster;

/**
 * @author MinhQuy
 */
@RestController
public class TimeSlotController {
    @Autowired
    TimeSlotServices timeSlotServices;

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/match/free-time", method = RequestMethod.GET)
    public ResponseEntity<Wrapper> getFreeTime(@RequestParam("field-owner-id") int fieldOwnerId, @RequestParam("field-type-id") int fieldTypeId,
                                               @RequestParam("date") String date) {
        Wrapper wrapper = new Wrapper(timeSlotServices.findFreeTimeByFieldOwnerTypeAndDate(fieldOwnerId, fieldTypeId, date), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/match/reserve-time-slot", method = RequestMethod.POST)
    public ResponseEntity reserveTimeSlot(@RequestBody InputReservationDTO inputReservationDTO) {
        Wrapper wrapper = new Wrapper(timeSlotServices.reserveTimeSlot(inputReservationDTO), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/match/match-upcoming", method = RequestMethod.GET)
    public ResponseEntity getMatchUpComing(@RequestParam("field-owner-id") int fieldOwnerId, @RequestParam("date") String date) {
        Wrapper wrapper = new Wrapper(timeSlotServices.findUpcomingReservationByDate(date, fieldOwnerId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/match/free-field", method = RequestMethod.GET)
    public ResponseEntity getFreeField(@RequestParam("field-owner-id") int fieldOwnerId, @RequestParam("field-type-id") int fieldTypeId,
                                       @RequestParam("date") String targetDate, @RequestParam("time") String targetTime) {
        Wrapper wrapper = new Wrapper(timeSlotServices.getListFreeFieldAtSpecificTime(targetDate, targetTime, fieldOwnerId, fieldTypeId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/match/set-field", method = RequestMethod.PUT)
    public ResponseEntity setFieldForMatch(@RequestParam("time-slot-id") int timeSlotId, @RequestParam("field-id") int fieldId) {
        Wrapper wrapper = new Wrapper(timeSlotServices.setFieldForTimeSlot(timeSlotId, fieldId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/match/cancel-reservation", method = RequestMethod.PUT)
    public ResponseEntity cancelReservationTimeSlot(@RequestParam("time-slot-id") int timeSlotId){
        Wrapper wrapper = new Wrapper(timeSlotServices.cancelReservationTimeSlot(timeSlotId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }
}
