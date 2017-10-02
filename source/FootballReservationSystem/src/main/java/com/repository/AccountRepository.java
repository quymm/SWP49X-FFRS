package com.repository;

import com.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by MinhQuy on 9/29/2017.
 */
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    AccountEntity findByIdAndStatus(int id, boolean status);
    List<AccountEntity> findAllByRoleAndStatus(String role, boolean status);
}
