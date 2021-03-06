package com.repository;

import com.entity.AccountEntity;
import com.entity.RatingOpponentEntity;
import com.entity.TourMatchEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface RatingOpponentRepository extends JpaRepository<RatingOpponentEntity, Integer> {
    List<RatingOpponentEntity> findByUserIdAndStatus(AccountEntity accountEntity, boolean status);

    List<RatingOpponentEntity> findByTourMatchIdAndStatus(TourMatchEntity tourMatchEntity, boolean status);

    RatingOpponentEntity findByUserIdAndTourMatchIdAndStatus(AccountEntity userAccountEntity, TourMatchEntity tourMatchEntity, boolean status);

    RatingOpponentEntity findByIdAndStatus(int id, boolean status);
}
