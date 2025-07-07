package com.plazoleta.plazoleta.infrastructure.mappers;

import com.plazoleta.plazoleta.domain.models.DishModel;
import com.plazoleta.plazoleta.infrastructure.entities.DishEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DishEntityMapper {
    DishEntity modelToEntity(DishModel dishModel);

    @Mapping(target = "restaurantId", source = "restaurantId")
    DishModel entityToModel(DishEntity dishEntity);
}
