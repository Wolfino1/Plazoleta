package com.plazoleta.plazoleta.application.dto.request;

import com.plazoleta.plazoleta.domain.models.OrderStatus;

public record ChangeOrderStatusRequest(OrderStatus status){
}
