package com.controller;

import com.dto.InputFieldDTO;
import com.entity.FieldEntity;
import com.services.FieldServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by MinhQuy on 9/24/2017.
 */
@RestController
public class FieldController {
    @Autowired
    FieldServices fieldServices;

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/field/createNewField", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity createNewField(@RequestBody InputFieldDTO inputFieldDTO){
        FieldEntity fieldEntity = fieldServices.createNewField(inputFieldDTO);
        return new ResponseEntity(fieldEntity, HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/field/getFieldByFieldOwnerId", method = RequestMethod.GET)
    public ResponseEntity getFieldByFieldOwnerId(@RequestParam("fieldOwnerId") int fieldOwnerId){
        List<FieldEntity> fieldEntityList = fieldServices.getFieldEntityByFieldOwnerId(fieldOwnerId);
        return new ResponseEntity(fieldEntityList, HttpStatus.FOUND);
    }
}
