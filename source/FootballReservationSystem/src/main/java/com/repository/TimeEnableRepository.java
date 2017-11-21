package com.repository;

import com.entity.AccountEntity;
import com.entity.FieldTypeEntity;
import com.entity.TimeEnableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface TimeEnableRepository extends JpaRepository<TimeEnableEntity, Integer> {
    List<TimeEnableEntity> findByFieldOwnerIdAndFieldTypeIdAndStatus(AccountEntity accountEntity, FieldTypeEntity fieldTypeEntity, boolean status);

    List<TimeEnableEntity> findByFieldOwnerIdAndStatus(AccountEntity accountEntity, boolean status);

    List<TimeEnableEntity> findByDateInWeekAndStatus(String dateInWeek, boolean status);

    @Query("SELECT t FROM TimeEnableEntity t WHERE t.fieldOwnerId = :accountEntity AND t.fieldTypeId = :fieldTypeEntity AND t.dateInWeek = :dateInWeek" +
            " AND t.optimal = :optimal AND t.status = :status ORDER BY t.startTime")
    List<TimeEnableEntity> findByFieldOwnerAndTypeAndDateInWeekAndOptimalOrderByStartTime(@Param("accountEntity") AccountEntity accountEntity, @Param("fieldTypeEntity") FieldTypeEntity fieldTypeEntity,
                                                                                          @Param("dateInWeek") String dateInWeek, @Param("optimal") boolean optimal, @Param("status") boolean status);

    @Query("SELECT t FROM TimeEnableEntity t WHERE t.fieldOwnerId = :accountEntity AND t.fieldTypeId = :fieldTypeEntity AND t.dateInWeek = :dateInWeek" +
            " AND t.status = :status ORDER BY t.startTime")
    List<TimeEnableEntity> findByFieldOwnerAndTypeAndDateInWeekOrderByStartTime(@Param("accountEntity") AccountEntity accountEntity, @Param("fieldTypeEntity") FieldTypeEntity fieldTypeEntity,
                                                                                @Param("dateInWeek") String dateInWeek, @Param("status") boolean status);

    @Query(value = "SELECT MAX(effective_date) FROM time_enable WHERE effective_date <= :targetDate", nativeQuery = true)
    Date getMaxEffectiveDate(@Param("targetDate") Date targetDate);

    @Query("SELECT t FROM TimeEnableEntity t WHERE t.fieldOwnerId = :accountEntity AND t.fieldTypeId = :fieldTypeEntity AND t.dateInWeek = :dateInWeek" +
            " AND t.optimal = :optimal AND t.effectiveDate = :effectiveDate AND t.status = :status ORDER BY t.startTime")
    List<TimeEnableEntity> findByFieldOwnerAndTypeAndDateInWeekAndOptimalAndEffectiveDateOrderByStartTime(@Param("accountEntity") AccountEntity accountEntity, @Param("fieldTypeEntity") FieldTypeEntity fieldTypeEntity,
                                                                                                          @Param("dateInWeek") String dateInWeek, @Param("optimal") boolean optimal, @Param("status") boolean status,
                                                                                                          @Param("effectiveDate") Date effectiveDate);

    @Query("SELECT t FROM TimeEnableEntity t WHERE t.fieldOwnerId = :accountEntity AND t.fieldTypeId = :fieldTypeEntity AND t.dateInWeek = :dateInWeek" +
            " AND t.effectiveDate = :effectiveDate AND t.status = :status ORDER BY t.startTime")
    List<TimeEnableEntity> findByFieldOwnerAndTypeAndDateInWeekAndEffectiveDateOrderByStartTime(@Param("accountEntity") AccountEntity accountEntity, @Param("fieldTypeEntity") FieldTypeEntity fieldTypeEntity,
                                                                                @Param("dateInWeek") String dateInWeek, @Param("status") boolean status,
                                                                                                @Param("effectiveDate") Date effectiveDate);


}
