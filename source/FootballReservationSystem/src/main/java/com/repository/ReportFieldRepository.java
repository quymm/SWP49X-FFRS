package com.repository;

import com.entity.AccountEntity;
import com.entity.ReportFieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportFieldRepository extends JpaRepository<ReportFieldEntity, Integer> {
    List<ReportFieldEntity> findByFieldOwnerIdAndStatus(AccountEntity fieldOwner, boolean status);
}
