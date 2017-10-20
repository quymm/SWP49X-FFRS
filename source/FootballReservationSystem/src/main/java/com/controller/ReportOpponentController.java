package com.controller;
import com.dto.InputReportOpponentDTO;
import com.entity.ReportOpponentEntity;
import com.services.ReportOpponentServices;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReportOpponentController {
    @Autowired
    ReportOpponentServices reportOpponentServices;

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/report/by-user-id", method = RequestMethod.GET)
    public ResponseEntity findByUserId(@RequestParam("user-id") int userId){
        return new ResponseEntity(reportOpponentServices.findByUserId(userId), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/report/to-opponent-id", method = RequestMethod.GET)
    public ResponseEntity findByOpponentId(@RequestParam("opponent-id") int opponentId){
        return new ResponseEntity(reportOpponentServices.findByOpponentId(opponentId), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/report/user-opponent-tourmatch", method = RequestMethod.GET)
    public ResponseEntity findByUserIdAndOpponentIdAndTourMatchIdAndStatus(@RequestParam("user-id") int userId,
                                                                           @RequestParam("opponent-id") int opponentId,
                                                                           @RequestParam("tourmatch-id") int tourMatchId){
        return new ResponseEntity(reportOpponentServices.findByUserIdAndOpponentIdAndTourMatchIdAndStatus(userId, opponentId,tourMatchId, true), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/report/create-new-report", method = RequestMethod.POST)
    public ResponseEntity createNewRe(@RequestBody InputReportOpponentDTO inputReportOpponentDTO){
        ReportOpponentEntity reportOpponentEntity = reportOpponentServices.createNewReportOpponent(inputReportOpponentDTO);
        return new ResponseEntity(reportOpponentEntity, HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/report/disable-report", method = RequestMethod.PUT)
    public ResponseEntity disableVoucher(@RequestParam("user-id") int userId,
                                         @RequestParam("opponent-id") int opponentId,
                                         @RequestParam("tourmatch-id") int tourMatchId) {
        ReportOpponentEntity reportOpponentEntity = reportOpponentServices.disableReport(userId, opponentId, tourMatchId, true);
        return new ResponseEntity(reportOpponentEntity, HttpStatus.OK);
    }
}
