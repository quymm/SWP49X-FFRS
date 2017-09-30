package com.repository;

import com.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by MinhQuy on 9/29/2017.
 */
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    AccountEntity findByIdAndStatus(int id, boolean status);
}
