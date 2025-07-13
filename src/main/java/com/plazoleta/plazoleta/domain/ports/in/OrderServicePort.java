package com.plazoleta.plazoleta.domain.ports.in;

import com.plazoleta.plazoleta.domain.models.OrderModel;
import com.plazoleta.plazoleta.domain.models.OrderStatus;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;

public interface OrderServicePort {
    void save(OrderModel orderModel);
    PagedResult<OrderModel> getOrdersByFilter (Long restaurantId,
                                               int page,
                                               int size,
                                               Long clientId,
                                               OrderStatus status);
    OrderModel assignEmployee(Long id, OrderModel updateFields);
    OrderModel changeStatus (Long id, OrderModel updateStatus);
}
