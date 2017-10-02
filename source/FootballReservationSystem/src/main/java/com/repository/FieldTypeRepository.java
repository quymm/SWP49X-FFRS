<<<<<<< HEAD
package com.repository;

import com.entity.FieldTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface FieldTypeRepository extends JpaRepository<FieldTypeEntity, Integer> {
    FieldTypeEntity findByIdAndStatus(int id, boolean status);
}
=======
package com.repository;

import com.entity.FieldTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by MinhQuy on 9/23/2017.
 */
public interface FieldTypeRepository extends JpaRepository<FieldTypeEntity, Integer> {
    FieldTypeEntity findByIdAndStatus(int id, boolean status);
}
>>>>>>> master
