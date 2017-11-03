package com.repository;

import com.entity.AccountEntity;
import com.entity.BillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface BillRepository extends JpaRepository<BillEntity, Integer> {
    BillEntity findByIdAndStatus(int id, boolean status);

    @Query("SELECT b FROM BillEntity b WHERE b.userId = :userId AND b.status = :status AND b.dateCharge  > :date")
    List<BillEntity>  findBillWithUserIdAndDateCharge(@Param("userId")AccountEntity userId, @Param("status") boolean status,
                                                      @Param("date") Date targetDate);

    @Query("SELECT b FROM BillEntity b WHERE b.fieldOwnerId = :fieldOwner AND b.status = :status AND b.dateCharge > :date")
    List<BillEntity> findBillWithFieldOwnerIdAndDateCharge(@Param("fieldOwner") AccountEntity owner, @Param("date") Date targetDate, @Param("status") boolean status);
}
