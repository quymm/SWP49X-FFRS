package com.repository;

import com.entity.AccountEntity;
import com.entity.ReportOpponentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by MinhQuy on 9/29/2017.
 */
public interface ReportOpponentRepository extends JpaRepository<ReportOpponentEntity, Integer> {
    List<ReportOpponentEntity> findByOpponentIdAndStatus(AccountEntity opponent, boolean status);
}

