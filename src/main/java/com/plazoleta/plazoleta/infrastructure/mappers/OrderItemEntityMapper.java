package com.plazoleta.plazoleta.infrastructure.mappers;

import com.plazoleta.plazoleta.domain.models.OrderItemModel;

import com.plazoleta.plazoleta.infrastructure.entities.OrderItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderItemEntityMapper {
    OrderItemEntity modelToEntity(OrderItemModel orderItemModel);
}
