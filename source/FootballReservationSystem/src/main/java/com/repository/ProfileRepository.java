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

//    @Query(value = "SELECT p FROM ProfileEntity p WHERE p.name LIKE :name AND p.status = :status")
//    List<ProfileEntity> searchByName(@Param("name") String name, @Param("status") boolean status);

    @Query(value = "SELECT * FROM profile where name like :name and status = :status limit 10", nativeQuery = true)
    List<ProfileEntity> searchByName(@Param("name") String name, @Param("status") boolean status);

    @Query(value = "SELECT p FROM ProfileEntity p WHERE p.latitude <= :latUp AND p.latitude >= :latDown AND p.longitude <= :longUp AND p.longitude >= :longDown AND p.status = :status")
    List<ProfileEntity> getByLocationWithLongLatAndDistance(@Param("latUp") double latUp, @Param("latDown") double latDown, @Param("longUp") double longUp, @Param("longDown") double longDown, @Param("status") boolean status);
}
