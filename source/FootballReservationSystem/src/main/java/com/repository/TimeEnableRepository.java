package com.repository;

import com.entity.AccountEntity;
import com.entity.FieldTypeEntity;
import com.entity.TimeEnableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface TimeEnableRepository extends JpaRepository<TimeEnableEntity, Integer> {
    List<TimeEnableEntity> findByFieldOwnerIdAndFieldTypeIdAndStatus(AccountEntity accountEntity, FieldTypeEntity fieldTypeEntity, boolean status);

    List<TimeEnableEntity> findByFieldOwnerIdAndStatus(AccountEntity accountEntity, boolean status);

    List<TimeEnableEntity> findByDateInWeekAndStatus(String dateInWeek, boolean status);
}
