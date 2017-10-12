package com.controller;

import com.dto.InputRatingOpponentDTO;
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

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/rating/by-userId", method = RequestMethod.GET)
    public ResponseEntity findByUserId(@RequestParam("user-id") int userId){
        return new ResponseEntity(ratingOpponentServices.findByUserId(userId), HttpStatus.OK);
    }

    @RequestMapping(value = "/swp49x-ffrs/rating/to-opponentId", method = RequestMethod.GET)
    public ResponseEntity findByOpponentId(@RequestParam("opponent-id") int opponentId){
        return new ResponseEntity(ratingOpponentServices.findByOpponentId(opponentId), HttpStatus.OK);
    }

    @RequestMapping(value = "/swp49x-ffrs/rating/user-opponent-tourmatch", method = RequestMethod.GET)
    public ResponseEntity findByUserIdAndOpponentIdAndTourMatchIdAndStatus(@RequestParam("user-id") int userId,
                                                                           @RequestParam("opponent-id") int opponentId,
                                                                           @RequestParam("tourmatch-id") int tourMatchId){
        return new ResponseEntity(ratingOpponentServices.findByUserIdAndOpponentIdAndTourMatchIdAndStatus(userId, opponentId,tourMatchId, true), HttpStatus.OK);
    }

//    createNewRatingOpponent
    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/rating/create-new-rating", method = RequestMethod.POST)
    public ResponseEntity createNewRatingOpponent(@RequestBody InputRatingOpponentDTO inputRatingOpponentDTO){
        RatingOpponentEntity ratingOpponentEntity = ratingOpponentServices.createNewRatingOpponent(inputRatingOpponentDTO);
        return new ResponseEntity(ratingOpponentEntity, HttpStatus.CREATED);
    }
}
