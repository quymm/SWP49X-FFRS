<<<<<<< HEAD
package com.repository;

import com.entity.FieldEntity;
import com.entity.TimeSlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface TimeSlotRepository extends JpaRepository<TimeSlotEntity, Integer> {
    List<TimeSlotEntity> findByDateAndFieldIdAndReserveStatusAndStatus(Date targetDate, FieldEntity fieldEntity, boolean reserveStatus, boolean status);
}
=======
package com.repository;

import com.entity.FieldEntity;
import com.entity.TimeSlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface TimeSlotRepository extends JpaRepository<TimeSlotEntity, Integer> {
    List<TimeSlotEntity> findByDateAndFieldIdAndReserveStatusAndStatus(Date targetDate, FieldEntity fieldEntity, boolean reserveStatus, boolean status);
}
>>>>>>> master
