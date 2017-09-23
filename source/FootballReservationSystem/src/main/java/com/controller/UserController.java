package com.controller;

import com.dto.InputUserDTO;
import com.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MinhQuy on 9/23/2017.
 */
@RestController()
public class UserController {
    @Autowired
    UserServices userServices;

    @RequestMapping(value = "user/createNewUser", method = RequestMethod.POST)
    public Map<String, Object> createNewUser(@RequestBody InputUserDTO inputUserDTO) {
        Map<String, Object> map = new HashMap();
        map.put("result", userServices.createNewUser(inputUserDTO));
        return map;
    }

    @RequestMapping(value = "user/getUserByUsername", method = RequestMethod.GET)
    public Map<String, Object> getUserByUsername(@RequestParam("username") String username) {
        Map<String, Object> map = new HashMap();
        map.put("result", userServices.getUserEntityByUserName(username));
        return map;
    }
}
