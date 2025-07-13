package com.plazoleta.plazoleta.application.service;

import com.plazoleta.plazoleta.application.dto.request.AssignEmployeeToOrderRequest;
import com.plazoleta.plazoleta.application.dto.request.ChangeOrderStatusRequest;
import com.plazoleta.plazoleta.application.dto.request.SaveOrderRequest;
import com.plazoleta.plazoleta.application.dto.response.AssignEmployeeToOrderResponse;
import com.plazoleta.plazoleta.application.dto.response.ChangeOrderStatusResponse;
import com.plazoleta.plazoleta.application.dto.response.OrderResponse;
import com.plazoleta.plazoleta.application.dto.response.SaveOrderResponse;
import com.plazoleta.plazoleta.domain.models.OrderStatus;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;

public interface OrderService {
    SaveOrderResponse save(SaveOrderRequest request);
    PagedResult<OrderResponse> getOrdersByFilter (Long restaurantId,
                                                  int page,
                                                  int size,
                                                  Long clientId,
                                                  OrderStatus status);
    AssignEmployeeToOrderResponse assignEmployeeToOrder(Long id, AssignEmployeeToOrderRequest request);
    ChangeOrderStatusResponse changeOrderStatus (Long id, ChangeOrderStatusRequest request);
}
