package com.repository;

import com.entity.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface PromotionRepository extends JpaRepository<PromotionEntity, Integer> {
}
