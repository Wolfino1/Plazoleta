package com.plazoleta.plazoleta.infrastructure.repositories.mysql;

import com.plazoleta.plazoleta.domain.models.OrderStatus;
import com.plazoleta.plazoleta.infrastructure.entities.OrderEntity;
import com.plazoleta.plazoleta.infrastructure.entities.OrderItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    boolean existsByClientIdAndStatusIn(Long clientId, List<OrderStatus> statuses);

    @Query("""
        SELECT DISTINCT o
          FROM OrderEntity o
          LEFT JOIN o.items i
         WHERE (:restaurantId IS NULL OR o.restaurant.id = :restaurantId)
           AND (:clientId     IS NULL OR o.clientId      = :clientId)
           AND (:status       IS NULL OR o.status        = :status)
           AND (:dishIds      IS NULL OR i.dish.id       IN :dishIds)
        """)
    Page<OrderEntity> findByFilters(
            @Param("restaurantId") Long restaurantId,
            @Param("clientId")     Long clientId,
            @Param("status")       OrderStatus status,
            @Param("dishIds")      List<Long> dishIds,
            Pageable pageable
    );
}
