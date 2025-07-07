package com.plazoleta.plazoleta.application.dto.response;

public record DishResponse (String name,
        Integer price,
        String description,
        String urlImage,
        String category,
        boolean active){
}
