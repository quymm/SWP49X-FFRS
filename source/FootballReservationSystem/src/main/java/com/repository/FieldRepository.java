package com.repository;

import com.entity.AccountEntity;
import com.entity.FieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface FieldRepository extends JpaRepository<FieldEntity, Integer> {
    List<FieldEntity> findByFieldOwnerIdAndStatus(AccountEntity accountEntity, boolean status);
    FieldEntity findByFieldOwnerIdAndNameAndStatus(AccountEntity fieldOwnerEntity, String fieldName, boolean status);
    FieldEntity findByIdAndStatus(int id, boolean status);
    List<FieldEntity> findAllByStatus(boolean status);
}
