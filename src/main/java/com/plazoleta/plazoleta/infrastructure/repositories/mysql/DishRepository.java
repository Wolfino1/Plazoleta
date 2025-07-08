package com.plazoleta.plazoleta.infrastructure.repositories.mysql;

import com.plazoleta.plazoleta.infrastructure.entities.DishEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DishRepository extends JpaRepository<DishEntity, Long> {

    @Query("""
      SELECT d
        FROM DishEntity d
       WHERE d.restaurant.id = :restaurantId
         AND (:name        IS NULL OR LOWER(d.name)        LIKE LOWER(CONCAT('%', :name, '%')))
         AND (:price       IS NULL OR d.price              = :price)
         AND (:description IS NULL OR LOWER(d.description) LIKE LOWER(CONCAT('%', :description, '%')))
         AND (:urlImage    IS NULL OR LOWER(d.urlImage)    LIKE LOWER(CONCAT('%', :urlImage, '%')))
         AND (:categoryId  IS NULL OR d.category.id        = :categoryId)
         AND d.active = :active
""")
    Page<DishEntity> findWithFilters(
            @Param("restaurantId")  Long restaurantId,
            @Param("name")          String name,
            @Param("price")         Integer price,
            @Param("description")   String description,
            @Param("urlImage")      String urlImage,
            @Param("categoryId")    Long categoryId,
            @Param("active")        boolean active,
            Pageable pageable
    );

}

