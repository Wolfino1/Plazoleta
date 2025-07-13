package com.plazoleta.plazoleta.domain.usecases;


import com.plazoleta.plazoleta.domain.exceptions.*;
import com.plazoleta.plazoleta.domain.models.*;
import com.plazoleta.plazoleta.domain.ports.in.OrderServicePort;
import com.plazoleta.plazoleta.domain.ports.out.DishPersistencePort;
import com.plazoleta.plazoleta.domain.ports.out.NotificationClientPort;
import com.plazoleta.plazoleta.domain.ports.out.OrderPersistencePort;
import com.plazoleta.plazoleta.domain.ports.out.RestaurantPersistencePort;
import com.plazoleta.plazoleta.domain.util.constants.DomainConstants;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;
import com.plazoleta.plazoleta.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.IllegalStateException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderUseCase implements OrderServicePort {

    private final OrderPersistencePort orderPersistencePort;
    private final DishPersistencePort dishPersistencePort;
    private final RestaurantPersistencePort restaurantPersistencePort;
    private final JwtUtil jwtUtil;
    private final NotificationClientPort notificationClient;


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
        String phone = orderModel.getPhoneNumber();
        if (phone == null) {
            throw new WrongArgumentException(DomainConstants.PHONENUMBER_NULL_MESSAGE);
        }
        if (phone.trim().isEmpty()) {
            throw new EmptyException(DomainConstants.PHONENUMBER_EMPTY_MESSAGE);
        }
        if (phone.length() > 13) {
            throw new MaxSizeExceededException(DomainConstants.MAX_SIZE_EXCEEDED_PHONE_NUMBER);
        }
        String phoneRegex = "^\\+?[0-9]{2}[0-9]{10}$";
        if (!phone.matches(phoneRegex)) {
            throw new WrongArgumentException(DomainConstants.WRONG_ARGUMENT_PHONE_MESSAGE);
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


    @Override
    public OrderModel changeStatus(Long id, OrderModel updateStatus) {
        if (updateStatus.getStatus() == null) {
            throw new WrongArgumentException(DomainConstants.ORDER_STATUS_MANDATORY);
        }

        if (updateStatus.getStatus() != OrderStatus.LISTO) {
            throw new WrongArgumentException(DomainConstants.ORDER_STATUS_INVALID);
        }

            Long employeeIdFromToken = jwtUtil.getEmployeeIdFromSecurityContext();

            OrderModel existingOrder = orderPersistencePort.getById(id);

            if (existingOrder == null) {
                throw new WrongArgumentException(DomainConstants.ORDER_NOT_FOUND);
            }

            if (!existingOrder.getEmployeeId().equals(employeeIdFromToken)) {
                throw new UnauthorizedException(DomainConstants.NOT_ALLOWED_TO_EDIT_ORDERS);
            }

             if (existingOrder.getStatus() == OrderStatus.LISTO) {
                return existingOrder;
             }

            if (existingOrder.getStatus() != OrderStatus.EN_PREPARACION) {
                throw new IllegalStateException(DomainConstants.ORDER_NOT_PREPARATION);
            }

            existingOrder.setStatus(updateStatus.getStatus());
            orderPersistencePort.changeOrderStatus(id, existingOrder);

                notificationClient.notifyOrderReady(
                existingOrder.getId(),
                existingOrder.getClientId(),
                existingOrder.getStatus().name(),
                existingOrder.getPinSecurity(),
                existingOrder.getPhoneNumber()
        );
            return existingOrder;
        }
    }


