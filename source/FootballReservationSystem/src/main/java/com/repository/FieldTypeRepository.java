package com.repository;

import com.entity.FieldTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface FieldTypeRepository extends JpaRepository<FieldTypeEntity, Integer> {
    FieldTypeEntity findByIdAndStatus(int id, boolean status);

    List<FieldTypeEntity> findAllByStatus(boolean status);
}
