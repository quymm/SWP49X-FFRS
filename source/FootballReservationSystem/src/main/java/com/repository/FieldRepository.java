package com.repository;

import com.entity.FieldEntity;
import com.entity.FieldOwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface FieldRepository extends JpaRepository<FieldEntity, Integer> {
    List<FieldEntity> getFieldEntitiesByFieldOwnerId(FieldOwnerEntity fieldOwnerEntity);
}
