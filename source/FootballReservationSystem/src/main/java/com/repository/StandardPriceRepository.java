package com.repository;

import com.entity.FieldTypeEntity;
import com.entity.StandardPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StandardPriceRepository extends JpaRepository<StandardPriceEntity, Integer> {

    List<StandardPriceEntity> getAllByStatus(boolean status);

    List<StandardPriceEntity> findByRushHourAndStatus(boolean rushHour, boolean status);

    StandardPriceEntity findByFieldTypeIdAndRushHourAndStatus(FieldTypeEntity fieldTypeEntity, boolean rushHour, boolean status);

    StandardPriceEntity findById(Integer id);
}
