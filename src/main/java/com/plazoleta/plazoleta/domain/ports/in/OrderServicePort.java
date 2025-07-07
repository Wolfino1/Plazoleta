package com.plazoleta.plazoleta.domain.ports.in;

import com.plazoleta.plazoleta.domain.models.OrderModel;

public interface OrderServicePort {
    void save(OrderModel orderModel);
}
