package com.repository;

import com.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Minh Quy on 3/6/2017.
 */
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    @Query(value = "SELECT p FROM ProductEntity p WHERE p.categoryCode = :categoryCode")
    List<ProductEntity> findByCategoryCode(@Param("categoryCode") String categoryCode);

    @Query(value = "SELECT p FROM ProductEntity p WHERE p.productCode = :productCode")
    ProductEntity findByProductCode(@Param("productCode") String productCode);
}
