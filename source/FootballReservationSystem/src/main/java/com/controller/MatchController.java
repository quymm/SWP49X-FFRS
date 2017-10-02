package com.controller;

import com.dto.InputMatchingRequestDTO;
import com.services.MatchServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by MinhQuy on 9/30/2017.
 */
@RestController
public class MatchController {
    @Autowired
    MatchServices matchServices;

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "match/getMatchByFieldIdAndDate", method = RequestMethod.GET)
    public ResponseEntity getMatchByFieldIdAndDate(@RequestParam("fieldId") int fieldId,
                                                   @RequestParam("targetDate")Date targetDate){
        return new ResponseEntity(matchServices.findMatchByFieldIdAndDate(targetDate, fieldId), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "match/getMatchByFieldOwnerIdAndDate", method = RequestMethod.GET)
    public ResponseEntity getMatchByFieldOwnerIdAndDate(@RequestParam("fieldOwnerId") int fieldOwnerId,
                                                        @RequestParam("targetDate") Date targetDate){
        return new ResponseEntity(matchServices.findMatchByFieldOwnerIdAndDate(targetDate, fieldOwnerId), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "match/createNewMatchingRequest", method = RequestMethod.POST)
    public ResponseEntity createNewMatchingRequest(@RequestBody InputMatchingRequestDTO inputMatchingRequestDTO){
        return new ResponseEntity(matchServices.createNewMatchingRequest(inputMatchingRequestDTO), HttpStatus.CREATED);
    }
}
