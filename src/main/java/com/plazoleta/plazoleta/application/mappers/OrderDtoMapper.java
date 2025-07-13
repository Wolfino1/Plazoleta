package com.plazoleta.plazoleta.application.mappers;

import com.plazoleta.plazoleta.application.dto.request.AssignEmployeeToOrderRequest;
import com.plazoleta.plazoleta.application.dto.request.ChangeOrderStatusRequest;
import com.plazoleta.plazoleta.application.dto.request.SaveOrderRequest;
import com.plazoleta.plazoleta.application.dto.request.UpdateDishStatusRequest;
import com.plazoleta.plazoleta.application.dto.response.AssignEmployeeToOrderResponse;
import com.plazoleta.plazoleta.application.dto.response.ChangeOrderStatusResponse;
import com.plazoleta.plazoleta.application.dto.response.OrderResponse;
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
}


