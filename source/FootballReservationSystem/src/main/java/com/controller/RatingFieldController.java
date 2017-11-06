package com.controller;

import com.dto.InputRatingFieldDTO;
import com.dto.Wrapper;
import com.services.RatingFieldServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RatingFieldController {
    @Autowired
    RatingFieldServices ratingFieldServices;

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/rating/rating-field", method = RequestMethod.POST)
    public ResponseEntity ratingField(@RequestBody InputRatingFieldDTO ratingFieldDTO){
        Wrapper wrapper = new Wrapper(ratingFieldServices.ratingField(ratingFieldDTO), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }
}
