package com.repository;

import com.entity.FavoritesFieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface FavoritesFieldRepository extends JpaRepository<FavoritesFieldEntity, Integer> {
}
