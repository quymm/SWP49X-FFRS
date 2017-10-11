package com.controller;

import com.dto.InputVoucherDTO;
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

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/voucher/managed-voucher", method = RequestMethod.POST)
    public ResponseEntity createNewVoucher(@RequestBody InputVoucherDTO inputVoucherDTO) {
        VoucherEntity voucherEntity = voucherServices.createNewVoucher(inputVoucherDTO);
        return new ResponseEntity(voucherEntity, HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/voucher/managed-voucher", method = RequestMethod.PUT)
    public ResponseEntity updateVoucher(@RequestBody InputVoucherDTO inputVoucherDTO, @RequestParam("voucher-id") int voucherId) {
        VoucherEntity voucherEntity = voucherServices.updateVoucher(inputVoucherDTO, voucherId);
        return new ResponseEntity(voucherEntity, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/voucher/managed-voucher-delete", method = RequestMethod.PUT)
    public ResponseEntity disableVoucher(@RequestParam("voucher-id") int voucherId) {
        VoucherEntity voucherEntity = voucherServices.disableVoucher(voucherId);
        return new ResponseEntity(voucherEntity, HttpStatus.OK);
    }
}
