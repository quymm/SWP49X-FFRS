package com.repository;

import com.entity.AccountEntity;
import com.entity.ReportOpponentEntity;
import com.entity.TourMatchEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by MinhQuy on 9/29/2017.
 */
public interface ReportOpponentRepository extends JpaRepository<ReportOpponentEntity, Integer> {
    List<ReportOpponentEntity> findByUserIdAndStatus(AccountEntity accountEntity, boolean status);
    List<ReportOpponentEntity> findByOpponentIdAndStatus(AccountEntity accountEntity, boolean status);
    List<ReportOpponentEntity> findByTourMatchIdAndStatus(TourMatchEntity tourMatchEntity, boolean status);

    ReportOpponentEntity findByUserIdAndOpponentIdAndTourMatchIdAndStatus(AccountEntity userAccountEntity, AccountEntity opponentAccountEntity, TourMatchEntity tourMatchEntity, boolean status);
}

