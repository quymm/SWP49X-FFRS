package com.repository;

import com.entity.AccountEntity;
import com.entity.FieldEntity;
import com.entity.FieldTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface FieldRepository extends JpaRepository<FieldEntity, Integer> {
    @Query("SELECT f FROM FieldEntity f WHERE f.fieldOwnerId = :fieldOwner AND f.dateFrom <= :targetDate AND f.dateTo >= :targetDate  AND f.status = :status")
    List<FieldEntity> findByFieldOwnerIdAndStatus(@Param("fieldOwner") AccountEntity fieldOwner, @Param("targetDate") Date targetDate, @Param("status") boolean status);

    FieldEntity findByIdAndStatus(int id, boolean status);
    List<FieldEntity> findAllByStatus(boolean status);
    List<FieldEntity> findByFieldOwnerIdAndFieldTypeIdAndStatus(AccountEntity fieldOwnerId, FieldTypeEntity fieldTypeId, boolean status);
    FieldEntity findByFieldOwnerIdAndFieldTypeIdAndNameAndStatus(AccountEntity fieldOwnerId, FieldTypeEntity fieldTypeId, String name, boolean status);

    @Query("SELECT f FROM FieldEntity f WHERE f.fieldOwnerId = :fieldOwner AND f.fieldTypeId = :fieldType AND f.dateFrom <= :targetDate AND f.status = :status")
    List<FieldEntity> getListFieldWithFieldOwnerTypeAndDate(@Param("fieldOwner") AccountEntity fieldOwner, @Param("fieldType") FieldTypeEntity fieldType, @Param("targetDate") Date targetDate, @Param("status") boolean status);

    @Query("SELECT f FROM FieldEntity f WHERE f.fieldOwnerId = :fieldOwner AND f.status = :status")
    List<FieldEntity> findByFieldOwnerId(@Param("fieldOwner") AccountEntity fieldOwner, @Param("status") boolean status);

}
