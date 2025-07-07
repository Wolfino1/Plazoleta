package com.plazoleta.plazoleta.application.dto.response;

public record OrderItemResponse(
        Long dishId,
        String dishName,
        Integer quantity,
        Integer unitPrice
){}