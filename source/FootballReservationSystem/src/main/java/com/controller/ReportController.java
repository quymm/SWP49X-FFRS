package com.controller;

import com.dto.InputReportFieldDTO;
import com.dto.InputReportOpponentDTO;
import com.dto.Wrapper;
import com.services.ReportServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReportController {
    @Autowired
    ReportServices reportServices;

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/report/report-opponent", method = RequestMethod.POST)
    public ResponseEntity reportOpponent(@RequestBody InputReportOpponentDTO reportOpponentDTO) {
        Wrapper wrapper = new Wrapper(reportServices.reportOpponent(reportOpponentDTO), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/report/report-opponent", method = RequestMethod.GET)
    public ResponseEntity getReportOfUser(@RequestParam("user-id") int userId) {
        Wrapper wrapper = new Wrapper(reportServices.getReportOfUser(userId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/report/report-field", method = RequestMethod.POST)
    public ResponseEntity reportField(@RequestBody InputReportFieldDTO reportFieldDTO) {
        Wrapper wrapper = new Wrapper(reportServices.reportField(reportFieldDTO), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/report/get-all", method = RequestMethod.GET)
    public ResponseEntity getAllOrderByNumOfReport() {
        Wrapper wrapper = new Wrapper(reportServices.getAccountOrderByNumOfReport(), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }
}
