package com.controller;

import com.config.Constant;
import com.dto.*;
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
    @RequestMapping(value = "/swp49x-ffrs/account/managed-field-owner", method = RequestMethod.PUT)
    public ResponseEntity updateFieldOwner(@RequestBody InputFieldOwnerDTO inputFieldOwnerDTO, @RequestParam("field-owner-id") int fieldOwnerId) {
        Wrapper wrapper = new Wrapper(accountServices.updateProfileFieldOwner(inputFieldOwnerDTO, fieldOwnerId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/managed-user", method = RequestMethod.POST)
    public ResponseEntity createNewUser(@RequestBody InputUserDTO inputUserDTO) {
        Wrapper wrapper = new Wrapper(accountServices.createNewUser(inputUserDTO), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/login", method = RequestMethod.GET)
    public ResponseEntity<Wrapper> checkLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
        Wrapper wrapper = new Wrapper(accountServices.checkLogin(username, password), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/managed-field-owner", method = RequestMethod.GET)
    public ResponseEntity getFieldOwnerById(@RequestParam("field-owner-id") int fieldOwnerId) {
        Wrapper wrapper = new Wrapper(accountServices.findAccountEntityByIdAndRole(fieldOwnerId, constant.getFieldOwnerRole()), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/managed-field-owner", method = RequestMethod.POST)
    public ResponseEntity createNewFieldOwner(@RequestBody InputFieldOwnerDTO inputFieldOwnerDTO) {
        Wrapper wrapper = new Wrapper(accountServices.createNewFieldOwner(inputFieldOwnerDTO), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/managed-staff", method = RequestMethod.POST)
    public ResponseEntity createNewStaff(@RequestBody InputStaffDTO inputStaffDTO) {
        Wrapper wrapper = new Wrapper(accountServices.createNewStaff(inputStaffDTO), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/managed-staff", method = RequestMethod.GET)
    public ResponseEntity getStaffById(@RequestParam("staff-id") int staffId) {
        Wrapper wrapper = new Wrapper(accountServices.findAccountEntityByIdAndRole(staffId, constant.getStaffRole()), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/managed-user", method = RequestMethod.GET)
    public ResponseEntity getUserById(@RequestParam("user-id") int userId) {
        Wrapper wrapper = new Wrapper(accountServices.findAccountEntityByIdAndRole(userId, constant.getUserRole()), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account", method = RequestMethod.GET)
    public ResponseEntity getAllAccountByRole(@RequestParam("role") String role) {
        Wrapper wrapper = new Wrapper(accountServices.findAccountByRole(role), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/top-10-field-owner", method = RequestMethod.GET)
    public ResponseEntity get10FieldOwnerNearest(@RequestParam("longitude") String longitude, @RequestParam("latitude") String latitude) {
        Wrapper wrapper = new Wrapper(accountServices.findMax10FieldOwnerNearByPosition(longitude, latitude), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/name", method = RequestMethod.GET)
    public ResponseEntity searchByNameAndRole(@RequestParam("name") String name, @RequestParam("role") String role) {
        Wrapper wrapper = new Wrapper(accountServices.findByNameLikeAndRole(name, role), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/money", method = RequestMethod.POST)
    public ResponseEntity insertMoney(@RequestBody InputDepositHistoryDTO inputDepositHistoryDTO) {
        Wrapper wrapper = new Wrapper(accountServices.depositMoney(inputDepositHistoryDTO), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/account/money", method = RequestMethod.GET)
    public ResponseEntity getDepositHistory(@RequestParam("account-id") int accountId, @RequestParam("role") String role){
        Wrapper wrapper = new Wrapper(accountServices.findDepositHistoryByAccountId(accountId, role), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

}
