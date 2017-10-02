package com.repository;

import com.entity.TimeSlotEntity;
import com.entity.TourMatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface TourMatchRepository extends JpaRepository<TourMatchEntity, Integer> {
    TourMatchEntity findByTimeSlotIdAndStatus(TimeSlotEntity timeSlotEntity, boolean status);
}
