package com.repository;

import com.entity.AccountEntity;
import com.entity.FieldTypeEntity;
import com.entity.TimeEnableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface TimeEnableRepository extends JpaRepository<TimeEnableEntity, Integer> {
    List<TimeEnableEntity> findByFieldOwnerIdAndFieldTypeIdAndStatus(AccountEntity accountEntity, FieldTypeEntity fieldTypeEntity, boolean status);

    List<TimeEnableEntity> findByFieldOwnerIdAndStatus(AccountEntity accountEntity, boolean status);

    List<TimeEnableEntity> findByDateInWeekAndStatus(String dateInWeek, boolean status);

    @Query("SELECT t FROM TimeEnableEntity t WHERE t.fieldOwnerId = :accountEntity AND t.fieldTypeId = :fieldTypeEntity AND t.dateInWeek = :dateInWeek" +
            " AND t.status = :status ORDER BY t.startTime")
    List<TimeEnableEntity> findByFieldOwnerAndTypeAndDateAndDateInWeekOrderByStartTime(@Param("accountEntity") AccountEntity accountEntity, @Param("fieldTypeEntity") FieldTypeEntity fieldTypeEntity,
                                                                                       @Param("dateInWeek") String dateInWeek, @Param("status") boolean status);
}
