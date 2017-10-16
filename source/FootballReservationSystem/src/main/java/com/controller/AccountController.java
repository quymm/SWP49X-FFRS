package com.controller;

import com.dto.InputFieldOwnerDTO;
import com.dto.InputUserDTO;
import com.dto.Wrapper;
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

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/managed-field-owner", method = RequestMethod.POST)
    public ResponseEntity createNewFieldOwner(@RequestBody InputFieldOwnerDTO inputFieldOwnerDTO) {
        AccountEntity fieldOwnerEntity = accountServices.createNewFieldOwner(inputFieldOwnerDTO);
        return new ResponseEntity(fieldOwnerEntity, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/managed-field-owner", method = RequestMethod.PUT)
    public ResponseEntity updateFieldOwner(@RequestBody InputFieldOwnerDTO inputFieldOwnerDTO, @RequestParam("field-owner-id") int fieldOwnerId) {
        AccountEntity fieldOwnerEntity = accountServices.updateProfileFieldOwner(inputFieldOwnerDTO, fieldOwnerId);
        return new ResponseEntity(fieldOwnerEntity, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/managed-user", method = RequestMethod.POST)
    public ResponseEntity createNewUser(@RequestBody InputUserDTO inputUserDTO){
        AccountEntity userEntity = accountServices.createNewUser(inputUserDTO);
        return new ResponseEntity(userEntity, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/managed-field-owner", method = RequestMethod.GET)
    public ResponseEntity getFieldOwnerById(@RequestParam("field-owner-id") int fieldOwnerId){
        AccountEntity accountEntity = accountServices.findAccountEntityById(fieldOwnerId, "owner");
        return new ResponseEntity(accountEntity, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/login-field-owner", method = RequestMethod.GET)
    public ResponseEntity<Wrapper> checkLoginFieldOwner(@RequestParam("username") String username, @RequestParam("password") String password){
        Wrapper wrapper = new Wrapper(accountServices.checkLogin(username, password, "owner"), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/login-user", method = RequestMethod.GET)
    public ResponseEntity<Wrapper> checkLoginUser(@RequestParam("username") String username, @RequestParam("password") String password){
        Wrapper wrapper = new Wrapper(accountServices.checkLogin(username, password, "user"), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/managed-user", method = RequestMethod.GET)
    public ResponseEntity getUserById(@RequestParam("user-id") int userId){
        AccountEntity accountEntity = accountServices.findAccountEntityById(userId, "user");
        return new ResponseEntity(accountEntity, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account", method = RequestMethod.GET)
    public ResponseEntity getAllAccountByRole(@RequestParam("role") String role){
        return new ResponseEntity(accountServices.findAccountByRole(role), HttpStatus.OK);
    }


}
