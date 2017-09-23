package com.repository;

import com.entity.MatchingRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface MatchingRequestRepository extends JpaRepository<MatchingRequestEntity, Integer> {
}
