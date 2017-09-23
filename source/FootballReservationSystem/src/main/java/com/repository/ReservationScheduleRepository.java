package com.repository;

import com.entity.ReservationScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface ReservationScheduleRepository extends JpaRepository<ReservationScheduleEntity, Integer> {
}
