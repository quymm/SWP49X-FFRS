package com.controller;

import com.services.MatchServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * Created by MinhQuy on 9/30/2017.
 */
public class MatchController {
    @Autowired
    MatchServices matchServices;

    @RequestMapping(value = "match/getMatchByFieldIdAndDate", method = RequestMethod.GET)
    public ResponseEntity getMatchByFieldIdAndDate(@RequestParam("fieldId") int fieldId,
                                                   @RequestParam("targetDate")Date targetDate){
        return new ResponseEntity(matchServices.findMatchByFieldIdAndDate(targetDate, fieldId), HttpStatus.FOUND);
    }
}
