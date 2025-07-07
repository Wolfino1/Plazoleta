package com.plazoleta.plazoleta.infrastructure.repositories.mysql;

import com.plazoleta.plazoleta.infrastructure.entities.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    @Query("""
      SELECT r
        FROM RestaurantEntity r
       WHERE (:name     IS NULL   OR LOWER(r.name)    LIKE LOWER(CONCAT('%', :name, '%')))
         AND (:logoUrl  IS NULL   OR LOWER(r.logoUrl) LIKE LOWER(CONCAT('%', :logoUrl, '%')))
    """)
    Page<RestaurantEntity> findWithFilters(
            @Param("name")    String name,
            @Param("logoUrl") String logoUrl,
            Pageable pageable
    );
}

