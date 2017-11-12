package com.repository;

import com.entity.AccountEntity;
import com.entity.ReportOpponentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by MinhQuy on 9/29/2017.
 */
public interface ReportOpponentRepository extends JpaRepository<ReportOpponentEntity, Integer> {
    List<ReportOpponentEntity> findByOpponentIdAndStatus(AccountEntity opponent, boolean status);

    @Query(value = "SELECT *, COUNT(id) FROM report_opponent GROUP BY opponent_id ORDER BY COUNT(id) DESC", nativeQuery = true)
    List<ReportOpponentEntity> getListReportOfUser();
}

