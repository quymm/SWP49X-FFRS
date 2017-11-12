package com.repository;

import com.entity.AccountEntity;
import com.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<ReportEntity, Integer> {
    List<ReportEntity> findByAccusedIdAndStatus(AccountEntity accusedId, boolean status);
}
