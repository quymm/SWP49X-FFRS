package com.repository;

import com.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by MinhQuy on 9/29/2017.
 */
public interface ProfileRepository extends JpaRepository<ProfileEntity, Integer> {
}
