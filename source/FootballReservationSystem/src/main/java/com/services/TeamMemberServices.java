package com.services;

import com.config.Constant;
import com.dto.InputTeamMemberDTO;
import com.entity.AccountEntity;
import com.entity.TeamMemberEntity;
import com.repository.TeamMemberRepository;
import com.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamMemberServices {
    @Autowired
    TeamMemberRepository teamMemberRepository;

    @Autowired
    AccountServices accountServices;

    @Autowired
    Constant constant;

    public TeamMemberEntity createNewTeamMember(InputTeamMemberDTO inputTeamMemberDTO){
        AccountEntity captain = accountServices.findAccountEntityByIdAndRole(inputTeamMemberDTO.getCaptainId(), constant.getUserRole());
        if(teamMemberRepository.findByCaptainIdAndPlayerNameAndStatus(captain, inputTeamMemberDTO.getPlayerName(), true) != null){
            throw new IllegalArgumentException("This player name is already in team!");
        }
        TeamMemberEntity teamMemberEntity = new TeamMemberEntity();
        teamMemberEntity.setCaptainId(captain);
        teamMemberEntity.setPlayerName(inputTeamMemberDTO.getPlayerName());
        teamMemberEntity.setPhone(inputTeamMemberDTO.getPhone());
        teamMemberEntity.setStatus(true);
        teamMemberEntity.setAddress(inputTeamMemberDTO.getAddress());
        teamMemberEntity.setLongitude(NumberUtils.parseFromStringToDouble(inputTeamMemberDTO.getLongitude()));
        teamMemberEntity.setLatitude(NumberUtils.parseFromStringToDouble(inputTeamMemberDTO.getLatitude()));
        return teamMemberRepository.save(teamMemberEntity);
    }

    public List<TeamMemberEntity> findTeamMemberListWithCaptainId(int captainId){
        AccountEntity captain = accountServices.findAccountEntityByIdAndRole(captainId, constant.getUserRole());
        return teamMemberRepository.findAllByCaptainIdAndStatus(captain, true);
    }

    public boolean removeTeamMember(int teamMemberId){
        TeamMemberEntity teamMemberEntity = teamMemberRepository.findByIdAndStatus(teamMemberId, true);
        teamMemberRepository.delete(teamMemberEntity);
        return true;
    }
}
