package com.plazoleta.plazoleta.application.dto.response;

import com.plazoleta.plazoleta.domain.models.OrderStatus;

public record AssignEmployeeToOrderResponse(
        Long id,
        Long employeeId,
        OrderStatus status
) {}
