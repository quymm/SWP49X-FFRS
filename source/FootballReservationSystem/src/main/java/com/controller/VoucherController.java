package com.controller;

import com.dto.InputVoucherDTO;
import com.dto.Wrapper;
import com.entity.VoucherEntity;
import com.services.VoucherServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class VoucherController {

    @Autowired
    VoucherServices voucherServices;

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/voucher/managed-voucher", method = RequestMethod.POST)
    public ResponseEntity createNewVoucher(@RequestBody InputVoucherDTO inputVoucherDTO) {
        Wrapper wrapper = new Wrapper(voucherServices.createNewVoucher(inputVoucherDTO), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/voucher/managed-voucher", method = RequestMethod.PUT)
    public ResponseEntity updateVoucher(@RequestBody InputVoucherDTO inputVoucherDTO, @RequestParam("voucher-id") int voucherId) {
        Wrapper wrapper = new Wrapper(voucherServices.updateVoucher(inputVoucherDTO, voucherId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/voucher/managed-voucher-delete", method = RequestMethod.PUT)
    public ResponseEntity disableVoucher(@RequestParam("voucher-id") int voucherId) {
        Wrapper wrapper = new Wrapper(voucherServices.disableVoucher(voucherId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }
}
