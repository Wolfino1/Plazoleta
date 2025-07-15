package com.plazoleta.plazoleta.application.dto.response;

import com.plazoleta.plazoleta.domain.models.OrderStatus;

public record OrderByIdResponse(
        Long id,
        Long clientId,
        OrderStatus status,
        Long employeeId
) {}
