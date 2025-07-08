package com.plazoleta.plazoleta.domain.ports.out;

import com.plazoleta.plazoleta.domain.models.OrderModel;
import com.plazoleta.plazoleta.domain.models.OrderStatus;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;

import java.util.List;

public interface OrderPersistencePort {
    boolean existsByClientAndStatusIn(Long clientId, List<OrderStatus> statuses);
    void save(OrderModel orderModel);
    PagedResult<OrderModel> getOrdersByFilter (Long restaurantId,
                                               int page,
                                               int size,
                                               Long clientId,
                                               OrderStatus status);
}
