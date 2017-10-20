package com.repository;

import com.entity.AccountEntity;
import com.entity.BillEntity;
import com.entity.FriendlyMatchEntity;
import com.entity.TourMatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Date;
import java.util.List;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface BillRepository extends JpaRepository<BillEntity, Integer> {
    BillEntity findByUserIdAndFriendlyMatchIdAndTourMatchIdAndStatus(AccountEntity userId, FriendlyMatchEntity friendlyMatch, TourMatchEntity tourMatch, boolean status);

}
