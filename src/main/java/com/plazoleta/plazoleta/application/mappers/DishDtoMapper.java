package com.plazoleta.plazoleta.application.mappers;

import com.plazoleta.plazoleta.application.dto.request.SaveDishRequest;
import com.plazoleta.plazoleta.application.dto.request.UpdateDishRequest;
import com.plazoleta.plazoleta.domain.models.DishModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DishDtoMapper {
    DishModel requestToModel(SaveDishRequest saveDishRequest);
    @Mapping(target = "price",       source = "price")
    @Mapping(target = "description", source = "description")
    DishModel updateRequestToModel(UpdateDishRequest updateDishRequest);
}
