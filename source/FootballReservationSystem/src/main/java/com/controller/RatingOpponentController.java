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
    @RequestMapping(value = "/swp49x-ffrs/rating/by-user-id", method = RequestMethod.GET)
    public ResponseEntity findByUserId(@RequestParam("user-id") int userId){
        Wrapper wrapper = new Wrapper(ratingOpponentServices.findByUserId(userId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/rating/to-opponent-id", method = RequestMethod.GET)
    public ResponseEntity findByOpponentId(@RequestParam("opponent-id") int opponentId){
        Wrapper wrapper = new Wrapper(ratingOpponentServices.findByOpponentId(opponentId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/rating/user-opponent-tourmatch", method = RequestMethod.GET)
    public ResponseEntity findByUserIdAndOpponentIdAndTourMatchIdAndStatus(@RequestParam("user-id") int userId,
                                                                           @RequestParam("opponent-id") int opponentId,
                                                                           @RequestParam("tourmatch-id") int tourMatchId){
        Wrapper wrapper = new Wrapper(ratingOpponentServices.findByUserIdAndOpponentIdAndTourMatchIdAndStatus(userId, opponentId, tourMatchId, true), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/rating/create-new-rating", method = RequestMethod.POST)
    public ResponseEntity createNewRatingOpponent(@RequestBody InputRatingOpponentDTO inputRatingOpponentDTO){
        Wrapper wrapper = new Wrapper(ratingOpponentServices.createNewRatingOpponent(inputRatingOpponentDTO), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }
}
