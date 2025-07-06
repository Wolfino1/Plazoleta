package com.plazoleta.plazoleta.infrastructure.repositories.mysql;

import com.plazoleta.plazoleta.infrastructure.entities.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
}
