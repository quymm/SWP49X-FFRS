package com.controller;


import com.dto.InputVoucherRecordDTO;
import com.entity.VoucherRecordEntity;
import com.services.VoucherRecordServices;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class VoucherRecordController {
    @Autowired
    VoucherRecordServices voucherRecordServices;

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/voucher/managed-voucher-record", method = RequestMethod.POST)
    public ResponseEntity createNewVoucherRecord(@RequestBody InputVoucherRecordDTO inputVoucherRecordDTO) {
        VoucherRecordEntity voucherRecordEntity = voucherRecordServices.createNewVoucherRecord(inputVoucherRecordDTO);
        return new ResponseEntity(voucherRecordEntity, HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/voucher/managed-voucher-record", method = RequestMethod.GET)
    public ResponseEntity findByUserId(@RequestParam("user-id") int userId) {
        List<VoucherRecordEntity> voucherRecordEntityList = voucherRecordServices.findByUserId(userId);
        return new ResponseEntity(voucherRecordEntityList, HttpStatus.OK);
    }
}
