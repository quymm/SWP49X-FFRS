package com.repository;

import com.entity.AccountEntity;
import com.entity.RatingFieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface RatingFieldRepository extends JpaRepository<RatingFieldEntity, Integer> {
    List<RatingFieldEntity> findByFieldOwnerIdAndStatus(AccountEntity fieldOwner, boolean status);
}
