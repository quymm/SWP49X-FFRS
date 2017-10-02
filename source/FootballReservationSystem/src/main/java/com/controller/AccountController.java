package com.controller;

import com.dto.InputFieldOwnerDTO;
import com.dto.InputUserDTO;
import com.entity.AccountEntity;
import com.services.AccountServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by MinhQuy on 9/24/2017.
 */
@RestController
public class AccountController {
    @Autowired
    AccountServices accountServices;

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/account/createNewFieldOwner", method = RequestMethod.POST)
    public ResponseEntity createNewFieldOwner(@RequestBody InputFieldOwnerDTO inputFieldOwnerDTO) {
        AccountEntity fieldOwnerEntity = accountServices.createNewFieldOwner(inputFieldOwnerDTO);
        return new ResponseEntity(fieldOwnerEntity, HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/account/createNewUser", method = RequestMethod.POST)
    public ResponseEntity createNewUser(@RequestBody InputUserDTO inputUserDTO){
        AccountEntity userEntity = accountServices.createNewUser(inputUserDTO);
        return new ResponseEntity(userEntity, HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/account/getAccountById", method = RequestMethod.GET)
    public ResponseEntity getFieldOwnerById(@RequestParam("accountId") int accountId){
        AccountEntity accountEntity = accountServices.findAccountEntityById(accountId);
        return new ResponseEntity(accountEntity, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/account/getAccountByRole", method = RequestMethod.GET)
    public ResponseEntity getAllFieldOwner(@RequestParam("role") String role){
        return new ResponseEntity(accountServices.findAccountByRole(role), HttpStatus.OK);
    }


}
