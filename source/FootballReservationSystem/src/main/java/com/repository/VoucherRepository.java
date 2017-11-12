package com.repository;

import com.entity.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface VoucherRepository extends JpaRepository<VoucherEntity, Integer> {
    VoucherEntity findByIdAndStatus(int id, boolean status);

    List<VoucherEntity> findAllByStatus(boolean status);
}
