package com.repository;

import com.entity.AccountEntity;
import com.entity.ProfileEntity;
import com.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by MinhQuy on 9/29/2017.
 */
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    AccountEntity findByIdAndStatus(int id, boolean status);

    @Query("SELECT a FROM AccountEntity a WHERE a.roleId = :roleEntity AND a.status = :status")
    List<AccountEntity> findAllByRoleAndStatus(@Param("roleEntity") RoleEntity roleEntity, @Param("status") boolean status);

    @Query("SELECT a FROM AccountEntity a WHERE a.id = :id AND a.roleId = :roleEntity AND a.status = :status")
    AccountEntity findByIdAndRole(@Param("id") int id, @Param("roleEntity") RoleEntity roleEntity, @Param("status") boolean status);

    @Query("SELECT a FROM AccountEntity a WHERE a.username = :username AND a.password = :password AND a.roleId = :roleEntity AND a.status = :status")
    AccountEntity findByUsernamePasswordAndRoleEntity(@Param("username") String username, @Param("password") String password, @Param("roleEntity") RoleEntity roleEntity, @Param("status") boolean status);

    AccountEntity findByUsernameAndStatusAndRoleId(String username, boolean status, RoleEntity roleEntity);

    AccountEntity findByProfileIdAndRoleIdAndStatus(ProfileEntity profileEntity, RoleEntity roleEntity, boolean status);
}
