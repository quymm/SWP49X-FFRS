package com.controller;

import com.dto.InputBillDTO;
import com.dto.Wrapper;
import com.services.BillServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BillController {
    @Autowired
    BillServices billServices;

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/bill/managed-bill", method = RequestMethod.POST)
    public ResponseEntity createNewBill(@RequestBody InputBillDTO inputBillDTO) {
        Wrapper wrapper = new Wrapper(billServices.createBill(inputBillDTO), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/bill/managed-bill", method = RequestMethod.GET)
    public ResponseEntity findById(@RequestParam("bill-id") int billId){
        Wrapper wrapper = new Wrapper(billServices.findById(billId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/bill/user-history", method = RequestMethod.GET)
    public ResponseEntity findBillIn7DateByUserId(@RequestParam("user-id") int userId){
        Wrapper wrapper = new Wrapper(billServices.findByUserIdIn7Date(userId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/bill/field-owner-history", method = RequestMethod.GET)
    public ResponseEntity findBillIn7DateByFieldOwnerId(@RequestParam("field-owner-id") int fieldOwnerId){
        Wrapper wrapper = new Wrapper(billServices.findByFieldOwnerIdIn7Date(fieldOwnerId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }




}
