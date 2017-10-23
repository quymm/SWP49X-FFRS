package com.controller;

import com.services.TestTimeSlotServices;
import com.entity.TimeSlotEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
public class TestTimeSlotController {
    @Autowired
    TestTimeSlotServices testTimeSlotServices;

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/test/merge-time-slot", method = RequestMethod.GET)
    public ResponseEntity mergeTimeSlot(@RequestParam("field-owner-id") int fieldOwnerId, @RequestParam("field-type-id") int fieldTypeId,
                                        @RequestParam("date") String date){
        List<TimeSlotEntity> listTimeSlotEntity = testTimeSlotServices.findTimeSlotByDate(date, fieldOwnerId, fieldTypeId);
        return new ResponseEntity(testTimeSlotServices.mergeTimeSlotInList(listTimeSlotEntity), HttpStatus.OK);
        //        TestTimeSlotServices testTimeSlotServices;
//        List<TimeSlotEntity> list = findTimeSlotByDate("10-12-2017", 1, 1);
//        mergeTimeSlot(list);
    }
}
