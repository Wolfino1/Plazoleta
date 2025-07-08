package com.plazoleta.plazoleta.application.dto.response;

public record DishResponse (String name,
        Integer price,
        String description,
        String urlImage,
        Long idCategory,
        boolean active){
}
