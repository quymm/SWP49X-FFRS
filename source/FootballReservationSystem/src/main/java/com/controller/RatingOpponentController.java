package com.controller;

import com.dto.InputRatingOpponentDTO;
import com.dto.Wrapper;
import com.entity.RatingOpponentEntity;
import com.services.RatingOpponentServices;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RatingOpponentController {
    @Autowired
    RatingOpponentServices ratingOpponentServices;

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/rating/rating-opponent", method = RequestMethod.GET)
    public ResponseEntity getRatingOpponentById(@RequestParam("rating-id") int ratingId){
        Wrapper wrapper = new Wrapper(ratingOpponentServices.findById(ratingId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/rating/rating-opponent", method = RequestMethod.POST)
    public ResponseEntity createNewRatingOpponent(@RequestBody InputRatingOpponentDTO inputRatingOpponentDTO){
        Wrapper wrapper = new Wrapper(ratingOpponentServices.createNewRatingOpponent(inputRatingOpponentDTO), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }
}
