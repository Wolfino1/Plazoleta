package com.plazoleta.plazoleta.domain.usecases;


import com.plazoleta.plazoleta.domain.exceptions.UnauthorizedAccessException;
import com.plazoleta.plazoleta.domain.exceptions.UnauthorizedException;
import com.plazoleta.plazoleta.domain.exceptions.WrongArgumentException;
import com.plazoleta.plazoleta.domain.models.*;
import com.plazoleta.plazoleta.domain.ports.in.OrderServicePort;
import com.plazoleta.plazoleta.domain.ports.out.DishPersistencePort;
import com.plazoleta.plazoleta.domain.ports.out.OrderPersistencePort;
import com.plazoleta.plazoleta.domain.exceptions.BusinessException;
import com.plazoleta.plazoleta.domain.ports.out.RestaurantPersistencePort;
import com.plazoleta.plazoleta.domain.util.constants.DomainConstants;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;
import com.plazoleta.plazoleta.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderUseCase implements OrderServicePort {

    private final OrderPersistencePort orderPersistencePort;
    private final DishPersistencePort dishPersistencePort;
    private final RestaurantPersistencePort restaurantPersistencePort;
    private final JwtUtil jwtUtil;

    @Override
    public void save(OrderModel orderModel) {
        boolean hasOpen = orderPersistencePort.existsByClientAndStatusIn(
                orderModel.getClientId(),
                List.of(OrderStatus.PENDIENTE, OrderStatus.EN_PREPARACION)
        );
        if (hasOpen) {
            throw new BusinessException(DomainConstants.ORDER_ALREADY_CREATED);
        }

        restaurantPersistencePort.findById(orderModel.getRestaurantId())
                .orElseThrow(() ->
                        new WrongArgumentException(DomainConstants.NON_EXISTING_RESTAURANT)
                );

        for (OrderItemModel item : orderModel.getItems()) {
            DishModel dish = dishPersistencePort
                    .findById(item.getDishId())
                    .orElseThrow(() ->
                            new WrongArgumentException(DomainConstants.NON_EXISTING_DISH)
                    );
            if (!dish.getRestaurantId().equals(orderModel.getRestaurantId())) {
                throw new WrongArgumentException(
                        DomainConstants.NON_EXISTING_DISH
                );
            }
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
            OrderStatus status
    ) {
        Long restaurantIdFromToken = jwtUtil.getRestaurantIdFromSecurityContext();
        if (restaurantIdFromToken == null || !restaurantIdFromToken.equals(restaurantId)) {
            throw new UnauthorizedException(DomainConstants.NOT_ALLOWED_TO_CHECK_OTHER_RESTAURANTS_ORDERS);
        }

        return orderPersistencePort.getOrdersByFilter(
                restaurantId,
                page,
                size,
                clientId,
                status
        );
    }

    @Override
    public OrderModel assignEmployee(Long id, OrderModel updateFields) {
        if (updateFields.getEmployeeId() == null) {
            throw new WrongArgumentException(DomainConstants.EMPLOYEE_ID_MANDATORY);
        }

        Long employeeIdFromToken = jwtUtil.getEmployeeIdFromSecurityContext();
        if (!updateFields.getEmployeeId().equals(employeeIdFromToken)) {
            throw new UnauthorizedException(DomainConstants.NOT_ALLOWED_TO_ASSIGN_ORDERS);
        }

        OrderModel existingOrder = orderPersistencePort.getById(id);
        if (existingOrder == null) {
            throw new WrongArgumentException(DomainConstants.ORDER_NOT_FOUND);
        }

        restaurantPersistencePort
                .findById(existingOrder.getRestaurantId())
                .orElseThrow(() -> new WrongArgumentException(
                        DomainConstants.NON_EXISTING_RESTAURANT
                ));

        Long restaurantIdFromToken = jwtUtil.getRestaurantIdFromSecurityContext();
        if (restaurantIdFromToken == null
                || !restaurantIdFromToken.equals(existingOrder.getRestaurantId())) {
            throw new UnauthorizedException(
                    DomainConstants.NOT_ALLOWED_TO_CHECK_OTHER_RESTAURANTS_ORDERS
            );
        }

        if (existingOrder.getStatus() != OrderStatus.PENDIENTE) {
            throw new IllegalStateException(DomainConstants.ORDER_NOT_PENDING);
        }

        existingOrder.setEmployeeId(updateFields.getEmployeeId());
        existingOrder.setStatus(OrderStatus.EN_PREPARACION);

        orderPersistencePort.assignOrder(id, existingOrder);
        return existingOrder;
    }

}

