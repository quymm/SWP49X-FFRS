package com.controller;

import com.dto.InputPromotionDTO;
import com.dto.Wrapper;
import com.services.PromotionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PromotionController {
    @Autowired
    PromotionServices promotionServices;

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/promotion/managed-promotion", method = RequestMethod.POST)
    public ResponseEntity createNewPromotion(@RequestBody InputPromotionDTO inputPromotionDTO){
        Wrapper wrapper = new Wrapper(promotionServices.createNewPromotion(inputPromotionDTO), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/promotion/date", method = RequestMethod.GET)
    public ResponseEntity getPromotionByDate(@RequestParam("target-date") String date){
        Wrapper wrapper = new Wrapper(promotionServices.getListPromotionByDate(date), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/promotion/field-owner", method = RequestMethod.GET)
    public ResponseEntity getPromotionByFieldOwner(@RequestParam("field-owner-id") int fieldOwnerId){
        Wrapper wrapper = new Wrapper(promotionServices.getListPromotionByFieldOwnerId(fieldOwnerId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/promotion/field-owner-and-date", method = RequestMethod.GET)
    public ResponseEntity getPromotionByFieldOwnerAndDate(@RequestParam("field-owner-id") int fieldOwnerId, @RequestParam("target-date") String dateStr){
        Wrapper wrapper = new Wrapper(promotionServices.getListPromotionByFieldOwnerAndDate(fieldOwnerId, dateStr), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }
}
