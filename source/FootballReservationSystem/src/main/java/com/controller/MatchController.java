package com.controller;

import com.dto.InputMatchingRequestDTO;
import com.dto.InputReserveTimeSlotDTO;
import com.dto.OutputReserveTimeSlotDTO;
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
    public synchronized ResponseEntity reserveFriendlyMatch(@RequestBody InputReserveTimeSlotDTO inputReserveTimeSlotDTO, @RequestParam("user-id") int userId) {
        Wrapper wrapper = new Wrapper(matchServices.reserveFriendlyMatch(inputReserveTimeSlotDTO, userId), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
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
    @RequestMapping(value = "/swp49x-ffrs/match/matching-request", method = RequestMethod.DELETE)
    public ResponseEntity cancelMatchingRequest(@RequestParam("matching-request-id") int matchingRequestId){
        Wrapper wrapper = new Wrapper(matchServices.cancelMatchingRequest(matchingRequestId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/match/suggest-opponent", method = RequestMethod.PUT)
    public ResponseEntity suggestOpponent(@RequestBody InputMatchingRequestDTO matchingRequestDTO) {
        Wrapper wrapper = new Wrapper(matchServices.suggestOpponent(matchingRequestDTO), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/match/choose-field", method = RequestMethod.POST)
    public ResponseEntity chooseSuitableField(@RequestBody InputMatchingRequestDTO inputMatchingRequestDTO, @RequestParam("matching-request-id") int matchingRequestId) {
        Wrapper wrapper = new Wrapper(matchServices.chooseSuitableField(inputMatchingRequestDTO, matchingRequestId), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/match/tour-match", method = RequestMethod.POST)
    public synchronized ResponseEntity reserveTourMatch(@RequestBody InputReserveTimeSlotDTO inputReserveTimeSlotDTO, @RequestParam("matching-request-id") int matchingRequestId) {
        Wrapper wrapper = new Wrapper(matchServices.reserveTourMatch(inputReserveTimeSlotDTO, matchingRequestId), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/match/tour-match", method = RequestMethod.GET)
    public ResponseEntity findTourMatchById(@RequestParam("tour-match-id") int tourMatchId){
        Wrapper wrapper = new Wrapper(matchServices.findTourMatchEntityById(tourMatchId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/match/warning-field", method = RequestMethod.GET)
    public ResponseEntity warningAboutPriorityFavoritesField(@RequestParam("user-id") int userId, @RequestParam("longitude") String longitude,
                                                             @RequestParam("latitude") String latitude, @RequestParam("expected-distance") int expectedDistance){
        Wrapper wrapper = new Wrapper(matchServices.warningAboutDistanceOfFavoritesField(userId, longitude, latitude, expectedDistance), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/match/matching-request", method = RequestMethod.GET)
    public ResponseEntity findMatchingRequestById(@RequestParam("matching-request-id") int matchingRequestId) {
        Wrapper wrapper = new Wrapper(matchServices.findMatchingRequestEntityById(matchingRequestId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/match/user-history", method = RequestMethod.GET)
    public ResponseEntity findMatchingRequestByUserId(@RequestParam("user-id") int userId){
        Wrapper wrapper = new Wrapper(matchServices.findMatchingRequestByUserId(userId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }
}
