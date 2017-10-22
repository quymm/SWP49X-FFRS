package com.services;

import com.entity.RoleEntity;
import com.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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
        RoleEntity roleEntity = roleRepository.findByRoleNameAndStatus(roleName, true);
        if(roleEntity == null){
            throw new EntityNotFoundException(String.format("Not found role have role name: %s", roleName));
        }
        return roleEntity;
    }

    public RoleEntity findById(int id) {
        RoleEntity roleEntity = roleRepository.findByIdAndStatus(id, true);
        if(roleEntity == null){
            throw new EntityNotFoundException(String.format("Not found role have id = %s", id));
        }
        return roleEntity;
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
