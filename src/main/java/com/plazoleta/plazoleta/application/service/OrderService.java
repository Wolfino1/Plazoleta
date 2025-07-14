package com.plazoleta.plazoleta.application.service;

import com.plazoleta.plazoleta.application.dto.request.*;
import com.plazoleta.plazoleta.application.dto.response.*;
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
    SaveCompleteOrderResponse completeOrderStatus(Long id, CompleteOrderRequest request);
    CancelOrderResponse cancelOrder (Long id, CancelOrderRequest cancelOrderRequest);
}
