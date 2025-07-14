package com.plazoleta.plazoleta.domain.ports.in;

import com.plazoleta.plazoleta.application.dto.response.SaveCompleteOrderResponse;
import com.plazoleta.plazoleta.domain.models.OrderModel;
import com.plazoleta.plazoleta.domain.models.OrderStatus;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;
import org.hibernate.query.Order;

public interface OrderServicePort {
    void save(OrderModel orderModelr);
    PagedResult<OrderModel> getOrdersByFilter (Long restaurantId,
                                               int page,
                                               int size,
                                               Long clientId,
                                               OrderStatus status);
    OrderModel assignEmployee(Long id, OrderModel updateFields);
    OrderModel changeStatus (Long id, OrderModel updateStatus);
    OrderModel completeOrder(Long orderId, Integer pinSecurity);
    OrderModel cancelOrder (Long id, OrderModel cancelOrder);
}
