package com.controller;

import com.dto.InputStandardPriceDTO;
import com.dto.Wrapper;
import com.services.StandardPriceServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class StandardPriceController {
    @Autowired
    StandardPriceServices standardPriceServices;

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/standard-price/managed-standard-price", method = RequestMethod.POST)
    public ResponseEntity updateStandardPrice(@RequestBody InputStandardPriceDTO inputStandardPriceDTO) {
        Wrapper wrapper = new Wrapper(standardPriceServices.updateStandardPrice(inputStandardPriceDTO), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/standard-price/managed-standard-price", method = RequestMethod.GET)
    public ResponseEntity getAllStandardPrice(@RequestParam("rushHour") boolean rushHour) {
        Wrapper wrapper = new Wrapper(standardPriceServices.getStandardPriceWithRushHour(rushHour), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }
}
