package com.repository;

import com.entity.TimeSlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface TimeSlotRepository extends JpaRepository<TimeSlotEntity, Integer> {
}
