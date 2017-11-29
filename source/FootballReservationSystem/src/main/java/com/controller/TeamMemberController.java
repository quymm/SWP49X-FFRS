package com.controller;

import com.dto.InputTeamMemberDTO;
import com.dto.Wrapper;
import com.services.TeamMemberServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TeamMemberController {
    @Autowired
    TeamMemberServices teamMemberServices;

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/team-member/managed-team-member", method = RequestMethod.POST)
    public ResponseEntity createNewTeamMember(@RequestBody InputTeamMemberDTO inputTeamMemberDTO){
        Wrapper wrapper = new Wrapper(teamMemberServices.createNewTeamMember(inputTeamMemberDTO), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/team-member/managed-team-member", method = RequestMethod.GET)
    public ResponseEntity getListTeamMemberWithCaptainId(@RequestParam("captain-id") int captainId){
        Wrapper wrapper = new Wrapper(teamMemberServices.findTeamMemberListWithCaptainId(captainId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

}
