package com.repository;

import com.entity.FieldOwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface FieldOwnerRepository extends JpaRepository<FieldOwnerEntity, Integer> {

    FieldOwnerEntity findById(int id);
}
