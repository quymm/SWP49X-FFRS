package com.repository;

import com.entity.AccountEntity;
import com.entity.FieldEntity;
import com.entity.FieldTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface FieldRepository extends JpaRepository<FieldEntity, Integer> {
    List<FieldEntity> findByFieldOwnerIdAndStatus(AccountEntity accountEntity, boolean status);
    FieldEntity findByFieldOwnerIdAndNameAndStatus(AccountEntity fieldOwnerEntity, String fieldName, boolean status);
    FieldEntity findByIdAndStatus(int id, boolean status);
    List<FieldEntity> findAllByStatus(boolean status);
    List<FieldEntity> findByFieldOwnerIdAndFieldTypeIdAndStatus(AccountEntity fieldOwnerId, FieldTypeEntity fieldTypeId, boolean status);
    FieldEntity findByFieldOwnerIdAndFieldTypeIdAndNameAndStatus(AccountEntity fieldOwnerId, FieldTypeEntity fieldTypeId, String name, boolean status);

    Integer countByFieldOwnerIdAndAndFieldTypeIdAndStatus(AccountEntity fieldOwnerEntity, FieldTypeEntity fieldTypeEntity, boolean status);
}
