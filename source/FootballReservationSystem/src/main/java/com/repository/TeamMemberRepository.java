package com.repository;

import com.entity.AccountEntity;
import com.entity.TeamMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMemberEntity, Integer> {
    List<TeamMemberEntity> findAllByCaptainIdAndStatus(AccountEntity captainId, boolean status);

    TeamMemberEntity findByCaptainIdAndPlayerNameAndStatus(AccountEntity captainId, String playerName, boolean status);
}
