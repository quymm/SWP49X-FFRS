package com.repository;

import com.entity.FieldTypeEntity;
import com.entity.MatchingRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface MatchingRequestRepository extends JpaRepository<MatchingRequestEntity, Integer> {
    @Query("SELECT m FROM MatchingRequestEntity m WHERE m.fieldTypeId = :fieldType AND m.date = :date AND m.userId.profileId.ratingScore BETWEEN startScore AND endScore" +
            " AND m.status = :status AND m.startTime BETWEEN :startTime AND :endTime")
    List<MatchingRequestEntity> findSimilarMatchingRequest(@Param("fieldType") FieldTypeEntity fieldTypeEntity, @Param("status") boolean status,
                                                           @Param("startScore") int startScore, @Param("endScore") int endScore,
                                                           @Param("date") Date date, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
    MatchingRequestEntity findByIdAndStatus(int id, boolean status);
}
