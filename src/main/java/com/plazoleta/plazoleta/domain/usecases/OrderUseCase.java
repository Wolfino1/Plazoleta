package com.plazoleta.plazoleta.domain.usecases;


import com.plazoleta.plazoleta.domain.models.OrderModel;
import com.plazoleta.plazoleta.domain.models.OrderStatus;
import com.plazoleta.plazoleta.domain.ports.in.OrderServicePort;
import com.plazoleta.plazoleta.domain.ports.out.OrderPersistencePort;
import com.plazoleta.plazoleta.domain.exceptions.BusinessException;
import com.plazoleta.plazoleta.domain.util.constants.DomainConstants;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderUseCase implements OrderServicePort {

    private final OrderPersistencePort orderPersistencePort;

    @Override
    public void save(OrderModel orderModel) {
        boolean hasOpen = orderPersistencePort.existsByClientAndStatusIn(
                orderModel.getClientId(),
                List.of(OrderStatus.PENDIENTE, OrderStatus.EN_PREPARACION)
        );
        if (hasOpen) {
            throw new BusinessException(DomainConstants.ORDER_ALREADY_CREATED);
        }
        orderModel.setStatus(OrderStatus.PENDIENTE);
        orderPersistencePort.save(orderModel);
    }

    @Override
    public PagedResult<OrderModel> getOrdersByFilter(
            Long restaurantId,
            int page,
            int size,
            Long clientId,
            OrderStatus status) {
        return orderPersistencePort.getOrdersByFilter(
                restaurantId,
                page,
                size,
                clientId,
                status
        );
    }

}
