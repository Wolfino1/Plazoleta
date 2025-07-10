package com.plazoleta.plazoleta.infrastructure.mappers;

import com.plazoleta.plazoleta.domain.models.OrderItemModel;
import com.plazoleta.plazoleta.domain.models.OrderModel;
import com.plazoleta.plazoleta.infrastructure.entities.OrderEntity;
import com.plazoleta.plazoleta.infrastructure.entities.OrderItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderEntityMapper {
    OrderEntity modelToEntity(OrderModel orderModel);

    @Mapping(source = "clientId", target = "clientId")
    @Mapping(source = "restaurant.id", target = "restaurantId")
    @Mapping(source = "items", target = "items")
    OrderModel entityToModel(OrderEntity orderEntity);

    @Mapping(source = "dish.id", target = "dishId")
    OrderItemModel entityToModel(OrderItemEntity orderItemEntity);
}
