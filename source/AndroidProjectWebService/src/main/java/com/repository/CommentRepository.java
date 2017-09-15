package com.repository;

import com.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Minh Quy on 3/7/2017.
 */
public interface CommentRepository extends JpaRepository<CommentEntity, Integer>{
    @Query(value = "SELECT c FROM CommentEntity c WHERE c.productCode = :productCode")
    List<CommentEntity> findByProductCode(@Param("productCode") String productCode);
}
