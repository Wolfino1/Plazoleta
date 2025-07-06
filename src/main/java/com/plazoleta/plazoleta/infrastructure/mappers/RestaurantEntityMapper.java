package com.plazoleta.plazoleta.infrastructure.mappers;


import com.plazoleta.plazoleta.domain.models.RestaurantModel;
import com.plazoleta.plazoleta.infrastructure.entities.RestaurantEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface RestaurantEntityMapper {
    RestaurantEntity modelToEntity(RestaurantModel categoryModel);
    RestaurantModel entityToModel(RestaurantEntity categoryEntity);
}