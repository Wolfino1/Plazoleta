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
   WHERE (:id         IS NULL   OR r.id = :id)
     AND (:name       IS NULL   OR LOWER(r.name)    LIKE LOWER(CONCAT('%', :name, '%')))
     AND (:logoUrl    IS NULL   OR LOWER(r.logoUrl) LIKE LOWER(CONCAT('%', :logoUrl, '%')))
     AND (:ownerId    IS NULL   OR r.ownerId = :ownerId)
""")
    Page<RestaurantEntity> findWithFilters(
            @Param("id")      Long id,
            @Param("name")    String name,
            @Param("logoUrl") String logoUrl,
            @Param("ownerId") Long ownerId,
            Pageable pageable
    );

}

