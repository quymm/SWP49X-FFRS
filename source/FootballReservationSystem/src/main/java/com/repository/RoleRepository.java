package com.repository;

import com.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * @author MinhQuy
 */
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    RoleEntity findByRoleNameAndStatus(String roleName, boolean status);

    RoleEntity findByIdAndStatus(int id, boolean status);

    List<RoleEntity> findByStatus(boolean status);
}
