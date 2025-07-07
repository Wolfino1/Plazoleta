package com.plazoleta.plazoleta.application.mappers;

import com.plazoleta.plazoleta.application.dto.request.SaveRestaurantRequest;
import com.plazoleta.plazoleta.application.dto.response.RestaurantResponse;
import com.plazoleta.plazoleta.domain.models.RestaurantModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantDtoMapper {
    RestaurantModel requestToModel(SaveRestaurantRequest saveCategoryRequest);
    RestaurantResponse modelToResponse(RestaurantModel RestaurantModel);
}