package com.repository;

import com.entity.AccountEntity;
import com.entity.DepositHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepositHistoryRepository extends JpaRepository<DepositHistoryEntity, Integer> {

    List<DepositHistoryEntity> findByUserIdAndStatus(AccountEntity accountId, boolean status);
}
