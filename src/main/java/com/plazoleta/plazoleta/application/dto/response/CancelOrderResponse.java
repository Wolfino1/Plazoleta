package com.plazoleta.plazoleta.application.dto.response;

import com.plazoleta.plazoleta.domain.models.OrderStatus;

public record CancelOrderResponse (OrderStatus status){
}
