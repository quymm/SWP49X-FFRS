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


}
