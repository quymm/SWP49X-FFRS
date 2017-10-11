package com.repository;

import com.entity.AccountEntity;
import com.entity.VoucherEntity;
import com.entity.VoucherRecordEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface VoucherRecordRepository extends JpaRepository<VoucherRecordEntity, Integer> {
//    List<VoucherEntity> findVoucherRecordByUserIdAndStatus(AccountEntity accountEntity, boolean status);
    List<VoucherRecordEntity> findByUserIdAndStatus(AccountEntity accountEntity, boolean status);
    VoucherRecordEntity findByIdAndStatus(int id, boolean status);
    VoucherRecordEntity findByUserIdAndVoucherIdAndStatus(AccountEntity userId, VoucherEntity voucherId, boolean status);



}
