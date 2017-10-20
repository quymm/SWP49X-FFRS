package com.controller;
import com.dto.InputPromotionDTO;
import com.entity.PromotionEntity;
import com.services.PromotionServices;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PromotionController {
    @Autowired
    PromotionServices promotionServices;

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/promotion/by-field-owner-id-and-field-type", method = RequestMethod.GET)
    public ResponseEntity findByFieldOwnerIdAndFieldTypeId(@RequestParam("field-owner-id") int fieldOwnerId,
                                                           @RequestParam("field-type") int fieldType){
        return new ResponseEntity(promotionServices.findByFieldOwnerIdAndFieldTypeId(fieldOwnerId, fieldType), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/promotion/disable-promotion", method = RequestMethod.PUT)
//    disablePromotion(int fieldOwnerId, int fieldTypeId, String sDateFrom, String sdateTo
    public ResponseEntity disablePromotion(@RequestParam("field-owner-id") int fieldOwnerId,
                                           @RequestParam("field-type") int fieldType,
                                           @RequestParam("date-from") String dateFrom,
                                           @RequestParam("date-to") String dateTo){
        return new ResponseEntity(promotionServices.disablePromotion(fieldOwnerId, fieldType, dateFrom, dateTo), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/swp49x-ffrs/promotion/create-new-promotion", method = RequestMethod.POST)
    public ResponseEntity createNewPromotion(@RequestBody InputPromotionDTO inputPromotionDTO){
        PromotionEntity promotionEntity = promotionServices.createPromotion(inputPromotionDTO);
        return new ResponseEntity(promotionEntity, HttpStatus.CREATED);
    }
}
