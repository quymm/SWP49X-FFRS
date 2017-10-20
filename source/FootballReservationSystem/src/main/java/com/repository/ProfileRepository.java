package com.repository;

import com.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by MinhQuy on 9/29/2017.
 */
public interface ProfileRepository extends JpaRepository<ProfileEntity, Integer> {
    ProfileEntity findByIdAndStatus(int id, boolean status);

    @Query("SELECT p FROM ProfileEntity p WHERE p.name LIKE :name AND p.status = :status")
    List<ProfileEntity> searchByName(@Param("name") String name, @Param("status") boolean status);
}
