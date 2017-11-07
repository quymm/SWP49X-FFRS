package com.controller;

import com.config.Constant;
import com.dto.InputFieldOwnerDTO;
import com.dto.InputUserDTO;
import com.dto.Wrapper;
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

    @Autowired
    Constant constant;

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/managed-field-owner", method = RequestMethod.POST)
    public ResponseEntity createNewFieldOwner(@RequestBody InputFieldOwnerDTO inputFieldOwnerDTO) {
        Wrapper wrapper = new Wrapper(accountServices.createNewFieldOwner(inputFieldOwnerDTO), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/managed-field-owner", method = RequestMethod.PUT)
    public ResponseEntity updateFieldOwner(@RequestBody InputFieldOwnerDTO inputFieldOwnerDTO, @RequestParam("field-owner-id") int fieldOwnerId) {
        Wrapper wrapper = new Wrapper(accountServices.updateProfileFieldOwner(inputFieldOwnerDTO, fieldOwnerId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/managed-user", method = RequestMethod.POST)
    public ResponseEntity createNewUser(@RequestBody InputUserDTO inputUserDTO){
        Wrapper wrapper = new Wrapper(accountServices.createNewUser(inputUserDTO), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/managed-field-owner", method = RequestMethod.GET)
    public ResponseEntity getFieldOwnerById(@RequestParam("field-owner-id") int fieldOwnerId){
        Wrapper wrapper = new Wrapper(accountServices.findAccountEntityById(fieldOwnerId, constant.getFieldOwnerRole()), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/login-field-owner", method = RequestMethod.GET)
    public ResponseEntity<Wrapper> checkLoginFieldOwner(@RequestParam("username") String username, @RequestParam("password") String password){
        Wrapper wrapper = new Wrapper(accountServices.checkLogin(username, password, constant.getFieldOwnerRole()), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/login-user", method = RequestMethod.GET)
    public ResponseEntity<Wrapper> checkLoginUser(@RequestParam("username") String username, @RequestParam("password") String password){
        Wrapper wrapper = new Wrapper(accountServices.checkLogin(username, password, constant.getUserRole()), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/managed-user", method = RequestMethod.GET)
    public ResponseEntity getUserById(@RequestParam("user-id") int userId){
        Wrapper wrapper = new Wrapper(accountServices.findAccountEntityById(userId, constant.getUserRole()), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account", method = RequestMethod.GET)
    public ResponseEntity getAllAccountByRole(@RequestParam("role") String role){
        Wrapper wrapper = new Wrapper(accountServices.findAccountByRole(role), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/top-10-field-owner", method = RequestMethod.GET)
    public ResponseEntity get10FieldOwnerNearest(@RequestParam("longitude") String longitude, @RequestParam("latitude") String latitude){
        Wrapper wrapper = new Wrapper(accountServices.findMax10FieldOwnerNearByPosition(longitude, latitude), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/name", method = RequestMethod.GET)
    public ResponseEntity searchByNameAndRole(@RequestParam("name") String name, @RequestParam("role") String role){
        Wrapper wrapper = new Wrapper(accountServices.findByNameLikeAndRole(name, role), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/money", method = RequestMethod.PUT)
    public ResponseEntity insertMoney(@RequestParam("account-id") int accountId, @RequestParam("role") String role, @RequestParam("balance") int balance){
        Wrapper wrapper = new Wrapper(accountServices.changeBalance(accountId, balance, role), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

}
