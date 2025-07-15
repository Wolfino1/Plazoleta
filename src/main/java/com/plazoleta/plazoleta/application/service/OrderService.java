package com.plazoleta.plazoleta.application.service;

import com.plazoleta.plazoleta.application.dto.request.*;
import com.plazoleta.plazoleta.application.dto.response.*;
import com.plazoleta.plazoleta.domain.models.OrderModel;
import com.plazoleta.plazoleta.domain.models.OrderStatus;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;

public interface OrderService {
    SaveOrderResponse save(SaveOrderRequest request);
    PagedResult<OrderResponse> getOrdersByFilter (Long restaurantId,
                                                  int page,
                                                  int size,
                                                  Long clientId,
                                                  OrderStatus status);
    AssignEmployeeToOrderResponse assignEmployeeToOrder(Long id, AssignEmployeeToOrderRequest request, String authHeader);
    ChangeOrderStatusResponse changeOrderStatus (Long id, ChangeOrderStatusRequest request, String authHeader);
    SaveCompleteOrderResponse completeOrderStatus(Long id, CompleteOrderRequest request, String authHeader);
    CancelOrderResponse cancelOrder (Long id, CancelOrderRequest cancelOrderRequest, String authHeader);
    OrderModel getOrderById(Long id);

}
