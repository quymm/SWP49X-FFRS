package com.services;

import com.entity.RoleEntity;
import com.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServices {
    @Autowired
    RoleRepository roleRepository;

    public RoleEntity createRole(String roleName) {
        if (roleName != null) {
            RoleEntity roleEntity = findByRoleName(roleName);
            if (roleEntity == null) {
                RoleEntity newRoleEntity = new RoleEntity();
                newRoleEntity.setRoleName(roleName);
                newRoleEntity.setStatus(true);
                return roleRepository.save(newRoleEntity);
            }
        }
        return null;
    }

    public RoleEntity findByRoleName(String roleName) {
        if (roleName != null) {
            RoleEntity roleEntity = roleRepository.findByRoleNameAndStatus(roleName, true);
            return roleEntity;
        }
        return null;
    }

    public RoleEntity findById(int id) {
        return roleRepository.findByIdAndStatus(id, true);
    }

    public List<RoleEntity> findAllRole() {
        return roleRepository.findByStatus(true);
    }

    public RoleEntity deleteRole(int id) {
        RoleEntity roleEntity = findById(id);
        roleEntity.setStatus(false);
        return roleRepository.save(roleEntity);
    }
}
