package com.repository;

import com.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Minh Quy on 3/6/2017.
 */
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {

}
