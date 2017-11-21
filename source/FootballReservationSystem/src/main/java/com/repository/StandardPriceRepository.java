package com.repository;

import com.entity.FieldTypeEntity;
import com.entity.StandardPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface StandardPriceRepository extends JpaRepository<StandardPriceEntity, Integer> {
    @Query("SELECT s FROM StandardPriceEntity s WHERE s.dateFrom <= :targetDate AND s.dateTo >= :targetDate AND s.fieldTypeId = :fieldType AND s.rushHour = :rushHour AND s.status = :status")
    StandardPriceEntity getStandartPriceWithTargetDateFieldTypeAndRushHour(@Param("targetDate")Date targetDate, @Param("status") boolean status, @Param("rushHour") boolean rushHour, @Param("fieldType")FieldTypeEntity fieldTypeEntity);

    List<StandardPriceEntity> getAllByStatus(boolean status);
}
