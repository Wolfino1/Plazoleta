package com.plazoleta.plazoleta.application.dto.request;

public record SaveDishRequest(
        String name,
        Integer price,
        String description,
        String urlImagen,
        String category,
        Long restaurantId
) {
}

