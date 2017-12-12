package com.repository;

import com.entity.AccountEntity;
import com.entity.FieldTypeEntity;
import com.entity.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface PromotionRepository extends JpaRepository<PromotionEntity, Integer> {
    List<PromotionEntity> findByFieldOwnerIdAndStatus(AccountEntity fieldOwner, boolean status);

    @Query("SELECT p FROM PromotionEntity p WHERE p.fieldOwnerId = :fieldOwner AND :date BETWEEN p.dateFrom AND p.dateTo AND p.status = :status")
    List<PromotionEntity> getPromotionByFieldOwnerAndTargetDate(@Param("fieldOwner") AccountEntity fieldOwner, @Param("date") Date date,
                                                                    @Param("status") boolean status);

    @Query("SELECT p FROM PromotionEntity p WHERE :date BETWEEN p.dateFrom AND p.dateTo AND p.status = :status")
    List<PromotionEntity> getPromotionByDate(@Param("date") Date date, @Param("status") boolean status);

    @Query("SELECT p FROM PromotionEntity p WHERE :date BETWEEN p.dateFrom AND p.dateTo AND p.fieldOwnerId = :fieldOwner AND p.fieldTypeId = :fieldType AND p.status = :status")
    PromotionEntity getPromotionByFieldOwnerTypeDateAndStatus(@Param("date") Date targetDate, @Param("fieldOwner") AccountEntity fieldOwner,
                                                                    @Param("fieldType")FieldTypeEntity fieldType, @Param("status") boolean status);

}
