package com.repository;

import com.entity.AccountEntity;
import com.entity.BlacklistOpponentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlacklistOpponentRepository extends JpaRepository<BlacklistOpponentEntity, Integer> {
    BlacklistOpponentEntity findByIdAndStatus(int id, boolean status);

    BlacklistOpponentEntity findByUserIdAndOpponentIdAndStatus(AccountEntity userId, AccountEntity opponentId, boolean status);

    List<BlacklistOpponentEntity> findByUserIdAndStatus(AccountEntity userId, boolean status);
}
