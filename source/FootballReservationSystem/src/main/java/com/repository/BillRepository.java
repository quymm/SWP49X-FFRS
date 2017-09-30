package com.repository;

import com.entity.BillEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface BillRepository extends JpaRepository<BillEntity, Integer> {
}
