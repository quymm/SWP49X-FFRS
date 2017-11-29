package com.repository;

import com.entity.FieldTypeEntity;
import com.entity.StandardPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface StandardPriceRepository extends JpaRepository<StandardPriceEntity, Integer> {

    List<StandardPriceEntity> getAllByStatus(boolean status);

    List<StandardPriceEntity> findByFieldTypeIdAndStatus(FieldTypeEntity fieldTypeEntity, boolean status);

    StandardPriceEntity findByFieldTypeIdAndRushHourAndStatus(FieldTypeEntity fieldTypeEntity, boolean rushHour, boolean status);
}
