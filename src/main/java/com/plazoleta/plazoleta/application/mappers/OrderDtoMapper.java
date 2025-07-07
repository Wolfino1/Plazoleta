package com.plazoleta.plazoleta.application.mappers;

import com.plazoleta.plazoleta.application.dto.request.SaveOrderRequest;
import com.plazoleta.plazoleta.domain.models.OrderModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderDtoMapper {
    OrderModel requestToModel(SaveOrderRequest saveOrderRequest);
}
