package com.repository;

import com.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface UserRepository extends JpaRepository<UserEntity, Integer>{
    @Query(value = "SELECT u FROM UserEntity u WHERE u.username = :username")
    UserEntity findUserEntityByUsername(@Param("username") String username);
}
