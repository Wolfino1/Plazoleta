package com.plazoleta.plazoleta.application.dto.response;

import com.plazoleta.plazoleta.domain.models.OrderStatus;

import java.util.List;

public record OrderResponse(Long orderId,
                            OrderStatus status,
                            List<OrderItemResponse> items) {
}
