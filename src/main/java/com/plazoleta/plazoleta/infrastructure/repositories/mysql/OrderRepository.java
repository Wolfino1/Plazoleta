package com.plazoleta.plazoleta.infrastructure.repositories.mysql;

import com.plazoleta.plazoleta.domain.models.OrderStatus;
import com.plazoleta.plazoleta.infrastructure.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    boolean existsByClientIdAndStatusIn(Long clientId, List<OrderStatus> statuses);
}
