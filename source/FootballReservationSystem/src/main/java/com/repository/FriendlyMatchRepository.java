<<<<<<< HEAD
package com.repository;

import com.entity.FriendlyMatchEntity;
import com.entity.TimeSlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface FriendlyMatchRepository extends JpaRepository<FriendlyMatchEntity, Integer> {
    FriendlyMatchEntity findByTimeSlotIdAndStatus(TimeSlotEntity timeSlotEntity, boolean status);
}
=======
package com.repository;

import com.entity.FriendlyMatchEntity;
import com.entity.TimeSlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface FriendlyMatchRepository extends JpaRepository<FriendlyMatchEntity, Integer> {
    FriendlyMatchEntity findByTimeSlotIdAndStatus(TimeSlotEntity timeSlotEntity, boolean status);
}
>>>>>>> master
