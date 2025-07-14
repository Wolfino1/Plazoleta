package com.plazoleta.plazoleta.application.mappers;

import com.plazoleta.plazoleta.application.dto.request.*;
import com.plazoleta.plazoleta.application.dto.response.*;
import com.plazoleta.plazoleta.domain.models.DishModel;
import com.plazoleta.plazoleta.domain.models.OrderItemModel;
import com.plazoleta.plazoleta.domain.models.OrderModel;
import org.aspectj.weaver.ast.Or;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderDtoMapper {
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    OrderModel requestToModel(SaveOrderRequest saveOrderRequest);
    OrderResponse modelToResponse(OrderModel orderModel);
    AssignEmployeeToOrderResponse modelToAssignEmployeeResponse(OrderModel orderModel);
    OrderModel assignEmployeeRequestToModel(AssignEmployeeToOrderRequest request);
    OrderModel changeOrderStatusRequestToModel (ChangeOrderStatusRequest request);
    ChangeOrderStatusResponse modelToChangeOrderResponse (OrderModel orderModel);
    SaveCompleteOrderResponse modelToSaveCompleteOrderResponse (OrderModel orderModel);
    OrderModel cancelOrder (CancelOrderRequest request);
    CancelOrderResponse modelToCancelOrderResponse (OrderModel orderModel);
}


