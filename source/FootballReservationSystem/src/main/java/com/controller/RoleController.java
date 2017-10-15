package com.controller;

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
        return new ResponseEntity(roleServices.createRole(roleName), HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/role/role-name", method = RequestMethod.GET)
    public ResponseEntity getRoleByRoleName(@RequestParam("role-name") String roleName){
        return new ResponseEntity(roleServices.findByRoleName(roleName), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/role/managed-role", method = RequestMethod.GET)
    public ResponseEntity getRoleById(@RequestParam("role-id") int roleId){
        return new ResponseEntity(roleServices.findById(roleId), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/role/all-role", method = RequestMethod.GET)
    public ResponseEntity findAllRole(){
        return new ResponseEntity(roleServices.findAllRole(), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/role/managed-role", method = RequestMethod.DELETE)
    public ResponseEntity deleteRole(@RequestParam("role-id") int roleId){
        return new ResponseEntity(roleServices.deleteRole(roleId), HttpStatus.OK);
    }



}
