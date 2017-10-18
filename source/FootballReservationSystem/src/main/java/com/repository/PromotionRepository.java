package com.repository;

import com.entity.AccountEntity;
import com.entity.FieldTypeEntity;
import com.entity.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Date;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface PromotionRepository extends JpaRepository<PromotionEntity, Integer> {
    List<PromotionEntity> findByFieldOwnerIdAndFieldTypeIdAndStatus(AccountEntity fieldOwnerAccountEntity, FieldTypeEntity fieldTypeEntity, boolean status);

    PromotionEntity findByFieldOwnerIdAndFieldTypeIdAndDateFromAndDateToAndStatus(AccountEntity fieldOwnerAccountEntity, FieldTypeEntity fieldTypeEntity, Date dateFrom, Date dateTo, boolean status);
}
