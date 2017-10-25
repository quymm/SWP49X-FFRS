package com.controller;

import com.dto.InputMatchingRequestDTO;
import com.dto.Wrapper;
import com.services.MatchServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MatchController {
    @Autowired
    MatchServices matchServices;

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/match/friendly-match", method = RequestMethod.POST)
    public ResponseEntity reserveFriendlyMatch(@RequestParam("time-slot-id") int timeSlotId, @RequestParam("user-id") int userId, @RequestParam("voucher-id") int voucherId) {
        Wrapper wrapper = new Wrapper(matchServices.reserveFriendlyMatch(timeSlotId, userId, voucherId), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/match/friendly-match", method = RequestMethod.GET)
    public ResponseEntity findFriendlyMatchById(@RequestParam("friendly-match-id") int friendlyMatchId){
        Wrapper wrapper = new Wrapper(matchServices.findFriendlyMatchEntityById(friendlyMatchId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/match/matching-request", method = RequestMethod.POST)
    public ResponseEntity createNewMatchingRequest(@RequestBody InputMatchingRequestDTO inputMatchingRequestDTO) {
        Wrapper wrapper = new Wrapper(matchServices.createNewMatchingRequest(inputMatchingRequestDTO), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/match/suggest-opponent", method = RequestMethod.GET)
    public ResponseEntity suggestOpponent(@RequestParam("user-id") int userId, @RequestParam("field-type-id") int fieldTypeId,
                                          @RequestParam("longitude") String longitude, @RequestParam("latitude") String latitude,
                                          @RequestParam("deviation-time") int deviationTime, @RequestParam("deviation-distance") int deviationDistance,
                                          @RequestParam("date") String date, @RequestParam("start-time") String startTime) {
        Wrapper wrapper = new Wrapper(matchServices.suggestOpponent(userId, fieldTypeId, longitude, latitude, date, startTime, deviationTime, deviationDistance), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/match/choose-field", method = RequestMethod.POST)
    public ResponseEntity chooseSuitableField(@RequestBody InputMatchingRequestDTO inputMatchingRequestDTO, @RequestParam("matching-request-id") int matchingRequestId,
                                              @RequestParam("deviation-distance") int deviationDistance) {
        Wrapper wrapper = new Wrapper(matchServices.chooseSuitableField(inputMatchingRequestDTO, matchingRequestId, deviationDistance), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/match/tour-match", method = RequestMethod.POST)
    public ResponseEntity reserveTourMatch(@RequestParam("time-slot-id") int timeSlotId, @RequestParam("matching-request-id") int matchingRequestId,
                                           @RequestParam("opponent-id") int opponentId, @RequestParam("voucher-id") int voucherId) {
        Wrapper wrapper = new Wrapper(matchServices.reserveTourMatch(timeSlotId, matchingRequestId, opponentId, voucherId), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/match/tour-match", method = RequestMethod.GET)
    public ResponseEntity findTourMatchById(@RequestParam("tour-match-id") int tourMatchId){
        Wrapper wrapper = new Wrapper(matchServices.findTourMatchEntityById(tourMatchId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/match/matching-request", method = RequestMethod.GET)
    public ResponseEntity findMatchingRequestById(@RequestParam("matching-request-id") int matchingRequestId) {
        Wrapper wrapper = new Wrapper(matchServices.findMatchingRequestEntityById(matchingRequestId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }
}
