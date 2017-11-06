package com.controller;

import com.dto.Wrapper;
import com.services.BlacklistOpponentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BlacklistOpponentController {
    @Autowired
    BlacklistOpponentServices blacklistOpponentServices;

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/blacklist/managed-blacklist", method = RequestMethod.POST)
    public ResponseEntity createNewBlacklist(@RequestParam("user-id") int userId, @RequestParam("opponent-id") int opponentId){
        Wrapper wrapper = new Wrapper(blacklistOpponentServices.createNewBlackListOpponent(userId, opponentId), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/blacklist/managed-blacklist", method = RequestMethod.GET)
    public ResponseEntity findBlacklistByUserId(@RequestParam("user-id") int userId){
        Wrapper wrapper = new Wrapper(blacklistOpponentServices.findBlacklistByUserId(userId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/blacklist/managed-blacklist", method = RequestMethod.DELETE)
    public ResponseEntity deleteBlacklist(@RequestParam("id") int id){
        Wrapper wrapper = new Wrapper(blacklistOpponentServices.removeBlacklist(id), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/blacklist/user-and-opponent", method = RequestMethod.GET)
    public ResponseEntity findByUserIdAndOpponentId(@RequestParam("user-id") int userId, @RequestParam("opponent-id") int opponentId){
        Wrapper wrapper = new Wrapper(blacklistOpponentServices.findBlacklistByUserIdAndOpponentId(userId, opponentId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }
}
