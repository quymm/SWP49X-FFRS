package com.controller;

import com.entity.FieldOwnerEntity;
import com.services.FieldOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by truonghuuthanh on 9/23/17.
 */
@RestController
@RequestMapping(value = "/fieldowner")
public class FieldOwnerController {

    @Autowired
    FieldOwnerService fieldOwnerService;


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value = "/login")
    public ResponseEntity login (@RequestParam("username") String username, @RequestParam("password") String password){
        FieldOwnerEntity fieldOwnerEntity = fieldOwnerService.fieldOwnerLogin(username, password);
        return new ResponseEntity(fieldOwnerEntity, HttpStatus.OK);
    }


//    public ResponseEntity register(){
//
//    }

}

