package com.plazoleta.plazoleta.infrastructure.client.dto;

import com.plazoleta.plazoleta.domain.models.OrderStatus;

import java.time.LocalDateTime;

public record CreateTraceabilityRequest(
        Long restaurantId,
        Long orderId,
        Long clientId,
        String clientEmail,
        LocalDateTime date,
        OrderStatus previousState,
        OrderStatus newState,
        Long employeeId,
        String employeeEmail
) {
}