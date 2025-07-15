package com.plazoleta.plazoleta.domain.ports.out;

import com.plazoleta.plazoleta.domain.models.DishModel;
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
    void assignOrder(Long id, OrderModel orderModel);
    void changeOrderStatus (Long id, OrderModel orderModel);
    OrderModel getById(Long id);
    OrderModel getUserById(Long id);
}
