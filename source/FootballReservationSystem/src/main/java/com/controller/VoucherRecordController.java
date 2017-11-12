package com.controller;


import com.dto.InputVoucherRecordDTO;
import com.dto.Wrapper;
import com.services.VoucherRecordServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class VoucherRecordController {
    @Autowired
    VoucherRecordServices voucherRecordServices;

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/voucher/managed-voucher-record", method = RequestMethod.POST)
    public ResponseEntity createNewVoucherRecord(@RequestBody InputVoucherRecordDTO inputVoucherRecordDTO) {
        Wrapper wrapper = new Wrapper(voucherRecordServices.exchangeVoucher(inputVoucherRecordDTO), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/voucher/managed-voucher-record", method = RequestMethod.GET)
    public ResponseEntity findByUserId(@RequestParam("user-id") int userId) {
        Wrapper wrapper = new Wrapper(voucherRecordServices.findByUserId(userId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }
}
