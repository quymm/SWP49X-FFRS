package com.controller;

import com.dto.Wrapper;
import com.services.RoleServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author MinhQuy
 */
@RestController
public class RoleController {
    @Autowired
    RoleServices roleServices;

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/role/managed-role", method = RequestMethod.POST)
    public ResponseEntity createRole(@RequestParam("role-name") String roleName){
        Wrapper wrapper = new Wrapper(roleServices.createRole(roleName), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/role/role-name", method = RequestMethod.GET)
    public ResponseEntity getRoleByRoleName(@RequestParam("role-name") String roleName){
        Wrapper wrapper = new Wrapper(roleServices.findByRoleName(roleName), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/role/managed-role", method = RequestMethod.GET)
    public ResponseEntity getRoleById(@RequestParam("role-id") int roleId){
        Wrapper wrapper = new Wrapper(roleServices.findById(roleId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/role/all-role", method = RequestMethod.GET)
    public ResponseEntity findAllRole(){
        Wrapper wrapper = new Wrapper(roleServices.findAllRole(), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/role/managed-role", method = RequestMethod.DELETE)
    public ResponseEntity deleteRole(@RequestParam("role-id") int roleId){
        Wrapper wrapper = new Wrapper(roleServices.deleteRole(roleId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }



}
