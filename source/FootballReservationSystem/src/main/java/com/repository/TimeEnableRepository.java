package com.repository;

import com.entity.AccountEntity;
import com.entity.FieldTypeEntity;
import com.entity.TimeEnableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TimeEnableRepository extends JpaRepository<TimeEnableEntity, Integer> {
    List<TimeEnableEntity> findByFieldOwnerIdAndFieldTypeIdAndStatus(AccountEntity accountEntity, FieldTypeEntity fieldTypeEntity, boolean status);

    List<TimeEnableEntity> findByFieldOwnerIdAndStatus(AccountEntity accountEntity, boolean status);

    @Query("SELECT t FROM TimeEnableEntity t WHERE t.fieldOwnerId = :accountEntity AND t.fieldTypeId = :fieldTypeEntity AND t.dateInWeek = :dateInWeek" +
            " AND t.optimal = :optimal AND t.dateFrom <= :targetDate AND t.status = :status ORDER BY t.startTime")
    List<TimeEnableEntity> findByFieldOwnerAndTypeAndDateInWeekAndOptimalAndTargetDateOrderByStartTime(@Param("accountEntity") AccountEntity accountEntity, @Param("fieldTypeEntity") FieldTypeEntity fieldTypeEntity,
                                                                                                       @Param("dateInWeek") String dateInWeek, @Param("targetDate") Date targetDate, @Param("optimal") boolean optimal, @Param("status") boolean status);

    @Query("SELECT t FROM TimeEnableEntity t WHERE t.fieldOwnerId = :accountEntity AND t.fieldTypeId = :fieldTypeEntity AND t.dateInWeek = :dateInWeek" +
            " AND t.dateFrom <= :targetDate AND t.status = :status ORDER BY t.startTime")
    List<TimeEnableEntity> findByFieldOwnerAndTypeAndDateInWeekAndTargetDateOrderByStartTime(@Param("accountEntity") AccountEntity accountEntity, @Param("fieldTypeEntity") FieldTypeEntity fieldTypeEntity,
                                                                                             @Param("dateInWeek") String dateInWeek, @Param("targetDate") Date targetDate,
                                                                                             @Param("status") boolean status);

//    @Query("SELECT t FROM TimeEnableEntity t WHERE t.fieldOwnerId = :accountEntity AND t.fieldTypeId = :fieldTypeEntity AND t.dateInWeek = :dateInWeek" +
//            " AND t.dateFrom = :effectiveDate AND t.status = :status ORDER BY t.startTime")
//    List<TimeEnableEntity> findByFieldOwnerAndTypeAndDateInWeekAndEffectiveDateOrderByStartTime(@Param("accountEntity") AccountEntity accountEntity, @Param("fieldTypeEntity") FieldTypeEntity fieldTypeEntity,
//                                                                                                @Param("dateInWeek") String dateInWeek, @Param("status") boolean status,
//                                                                                                @Param("effectiveDate") Date effectiveDate);


}
