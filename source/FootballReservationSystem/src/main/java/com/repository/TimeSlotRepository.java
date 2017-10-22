package com.repository;

import com.entity.AccountEntity;
import com.entity.FieldEntity;
import com.entity.FieldTypeEntity;
import com.entity.TimeSlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface TimeSlotRepository extends JpaRepository<TimeSlotEntity, Integer> {
    List<TimeSlotEntity> findByFieldOwnerIdAndReserveStatusAndDateAndStatus(AccountEntity accountEntity, boolean reservationStatus,
                                                                                          Date targetDate, boolean status);

    Integer countByFieldOwnerIdAndFieldTypeIdAndDateAndStatus(AccountEntity accountEntity, FieldTypeEntity fieldTypeEntity,
                                                              Date targetDate, boolean status);

    @Query("SELECT t FROM TimeSlotEntity t WHERE t.fieldOwnerId = :fieldOwner AND t.fieldTypeId = :fieldType AND t.date = :date AND t.startTime <= :time AND t.endTime > :time AND t.reserveStatus = :reserveStatus AND t.status = :status")
    List<TimeSlotEntity> findTimeSlotHaveMatch(@Param("fieldOwner") AccountEntity fieldOwner, @Param("fieldType") FieldTypeEntity fieldType,
                                           @Param("date") Date date, @Param("time") Date time, @Param("reserveStatus") boolean reserveStatus,
                                           @Param("status") boolean status);

    TimeSlotEntity findByIdAndStatus(int id, boolean status);

    List<TimeSlotEntity> findByFieldOwnerIdAndFieldTypeIdAndDateAndReserveStatusAndStatusOrderByStartTime(AccountEntity fieldOwnerEntity,
                                                                                                          FieldTypeEntity fieldTypeEntity,
                                                                                                          Date targetDate, boolean reservationStatus,
                                                                                                          boolean status);

}
