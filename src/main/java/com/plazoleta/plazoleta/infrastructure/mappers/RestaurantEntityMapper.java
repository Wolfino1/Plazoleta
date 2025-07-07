package com.plazoleta.plazoleta.infrastructure.mappers;


import com.plazoleta.plazoleta.domain.models.RestaurantModel;
import com.plazoleta.plazoleta.infrastructure.entities.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RestaurantEntityMapper {

    @Mapping(target = "dishes", ignore = true)
    RestaurantEntity modelToEntity(RestaurantModel restaurantModel);

    RestaurantModel entityToModel(RestaurantEntity restaurantEntity);
}