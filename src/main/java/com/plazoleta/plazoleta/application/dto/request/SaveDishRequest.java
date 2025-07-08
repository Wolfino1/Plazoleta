package com.plazoleta.plazoleta.application.dto.request;

public record SaveDishRequest(
        String name,
        Integer price,
        String description,
        String urlImage,
        Long idCategory,
        Long restaurantId
) {
}

