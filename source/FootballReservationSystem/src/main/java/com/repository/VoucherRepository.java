package com.repository;

import com.entity.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface VoucherRepository extends JpaRepository<VoucherEntity, Integer> {
}
