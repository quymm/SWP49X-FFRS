package com.repository;

import com.entity.AccountEntity;
import com.entity.FieldEntity;
import com.entity.FieldTypeEntity;
import com.entity.TimeSlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface TimeSlotRepository extends JpaRepository<TimeSlotEntity, Integer> {
    List<TimeSlotEntity> findByFieldOwnerIdAndFieldTypeIdAndDateAndReserveStatusAndStatus(AccountEntity accountEntity,
                                                                                          FieldTypeEntity fieldTypeEntity,
                                                                                          Date targetDate, boolean reservateStatus, boolean status);
    Integer countByFieldOwnerIdAndFieldTypeIdAndDateAndStatus(AccountEntity accountEntity, FieldTypeEntity fieldTypeEntity,
                                                              Date targetDate, boolean status);
}
