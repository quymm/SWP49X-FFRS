package com.repository;

import com.entity.AccountEntity;
import com.entity.FavoritesFieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface FavoritesFieldRepository extends JpaRepository<FavoritesFieldEntity, Integer> {
    List<FavoritesFieldEntity> findByUserIdAndStatus(AccountEntity user, boolean status);

    FavoritesFieldEntity findByUserIdAndFieldOwnerIdAndStatus(AccountEntity user, AccountEntity fieldOwner, boolean status);

    FavoritesFieldEntity findByIdAndStatus(int id, boolean status);
}
