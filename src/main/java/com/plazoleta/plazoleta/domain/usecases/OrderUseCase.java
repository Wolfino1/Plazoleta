package com.plazoleta.plazoleta.domain.usecases;

import com.plazoleta.plazoleta.domain.exceptions.*;
import com.plazoleta.plazoleta.domain.models.*;
import com.plazoleta.plazoleta.domain.ports.in.OrderServicePort;
import com.plazoleta.plazoleta.domain.ports.out.*;
import com.plazoleta.plazoleta.domain.util.constants.DomainConstants;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;
import com.plazoleta.plazoleta.infrastructure.client.dto.CreateTraceabilityRequest;
import com.plazoleta.plazoleta.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.IllegalStateException;
import java.time.LocalDateTime;
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
    private final TraceabilityClientPort traceabilityClientPort;

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
    public OrderModel assignEmployee(Long id, OrderModel updateFields, String authHeader) {
        if (updateFields.getEmployeeId() == null) {
            throw new WrongArgumentException(DomainConstants.EMPLOYEE_ID_MANDATORY);
        }
        Long employeeIdFromToken = jwtUtil.getEmployeeIdFromSecurityContext();
        if (!updateFields.getEmployeeId().equals(employeeIdFromToken)) {
            throw new UnauthorizedException(DomainConstants.NOT_ALLOWED_TO_ASSIGN_ORDERS);
        }
        OrderModel existing = orderPersistencePort.getById(id);
        if (existing == null) {
            throw new WrongArgumentException(DomainConstants.ORDER_NOT_FOUND);
        }
        restaurantPersistencePort.findById(existing.getRestaurantId())
                .orElseThrow(() -> new WrongArgumentException(DomainConstants.NON_EXISTING_RESTAURANT));
        Long restaurantIdFromToken = jwtUtil.getRestaurantIdFromSecurityContext();
        if (restaurantIdFromToken == null || !restaurantIdFromToken.equals(existing.getRestaurantId())) {
            throw new UnauthorizedException(DomainConstants.NOT_ALLOWED_TO_CHECK_OTHER_RESTAURANTS_ORDERS);
        }
        if (existing.getStatus() != OrderStatus.PENDIENTE) {
            throw new IllegalStateException(DomainConstants.ORDER_NOT_PENDING);
        }
        OrderStatus previousStatus = existing.getStatus(); // ANTES de cambiar nada

        existing.setEmployeeId(updateFields.getEmployeeId());
        existing.setStatus(OrderStatus.EN_PREPARACION);

        orderPersistencePort.assignOrder(id, existing);

        CreateTraceabilityRequest traceReq = new CreateTraceabilityRequest(
                existing.getId(),
                existing.getClientId(),
                null,
                LocalDateTime.now(),
                previousStatus,
                existing.getStatus(),
                existing.getEmployeeId(),
                null
        );

        traceabilityClientPort.createTrace(traceReq, authHeader);

        return existing;
    }

    @Override
    public OrderModel changeStatus(Long id, OrderModel updateStatus, String authHeader) {
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

        OrderStatus previousStatus = existingOrder.getStatus(); // Guardar antes de cambiar

        existingOrder.setStatus(updateStatus.getStatus());
        orderPersistencePort.changeOrderStatus(id, existingOrder);

        notificationClient.notifyOrderReady(
                existingOrder.getId(),
                existingOrder.getClientId(),
                existingOrder.getStatus().name(),
                existingOrder.getPinSecurity(),
                existingOrder.getPhoneNumber()
        );

        CreateTraceabilityRequest traceReq = new CreateTraceabilityRequest(
                existingOrder.getId(),
                existingOrder.getClientId(),
                null,
                LocalDateTime.now(),
                previousStatus,
                existingOrder.getStatus(),
                existingOrder.getEmployeeId(),
                null
        );
        traceabilityClientPort.createTrace(traceReq, authHeader);

        return existingOrder;
    }

    @Override
    public OrderModel completeOrder(Long orderId, Integer pinSecurity, String authHeader) {
        OrderModel existingOrder = orderPersistencePort.getById(orderId);
        if (existingOrder == null) {
            throw new WrongArgumentException(DomainConstants.ORDER_NOT_FOUND);
        }
        if (pinSecurity == null) {
            throw new WrongArgumentException(DomainConstants.ORDER_PIN_MANDATORY);
        }
        Long employeeId = jwtUtil.getEmployeeIdFromSecurityContext();
        if (!existingOrder.getEmployeeId().equals(employeeId)) {
            throw new UnauthorizedException(DomainConstants.NOT_ALLOWED_TO_EDIT_ORDERS);
        }

        if (existingOrder.getStatus() == OrderStatus.ENTREGADO) {
            throw new IllegalStateException(DomainConstants.ORDER_ALREADY_COMPLETED);
        }
        if (existingOrder.getStatus() != OrderStatus.LISTO) {
            throw new IllegalStateException(DomainConstants.ORDER_NOT_READY);
        }

        if (!existingOrder.getPinSecurity().equals(pinSecurity)) {
            throw new WrongArgumentException(DomainConstants.INVALID_SECURITY_PIN);
        }

        OrderStatus previousStatus = existingOrder.getStatus();

        existingOrder.setStatus(OrderStatus.ENTREGADO);
        orderPersistencePort.changeOrderStatus(orderId, existingOrder);

        CreateTraceabilityRequest traceReq = new CreateTraceabilityRequest(
                existingOrder.getId(),
                existingOrder.getClientId(),
                null,
                LocalDateTime.now(),
                previousStatus,
                existingOrder.getStatus(),
                existingOrder.getEmployeeId(),
                null
        );
        traceabilityClientPort.createTrace(traceReq, authHeader);
        return existingOrder;
    }

    @Override
    public OrderModel cancelOrder(Long id, OrderModel cancelOrder, String authHeader) {

        OrderModel orderCanceled = orderPersistencePort.getById(id);

        if (orderCanceled.getStatus() == null) {
            throw new WrongArgumentException(DomainConstants.ORDER_STATUS_MANDATORY);
        }

        if (orderCanceled.getStatus() != OrderStatus.PENDIENTE) {
            throw new WrongArgumentException(DomainConstants.ORDER_STATUS_NO_VALID);
        }

        Long clientIdFromToken = jwtUtil.getClientIdFromSecurityContext();

        OrderModel existingOrder = orderPersistencePort.getById(id);

        if (existingOrder == null) {
            throw new WrongArgumentException(DomainConstants.ORDER_NOT_FOUND);
        }

        if (!existingOrder.getClientId().equals(clientIdFromToken)) {
            throw new UnauthorizedException(DomainConstants.NOT_ALLOWED_TO_EDIT_ORDERS);
        }
        OrderStatus previousStatus = existingOrder.getStatus();

        existingOrder.setStatus(OrderStatus.CANCELADO);
        orderPersistencePort.changeOrderStatus(id, existingOrder);
        CreateTraceabilityRequest traceReq = new CreateTraceabilityRequest(
                existingOrder.getId(),
                existingOrder.getClientId(),
                null,
                LocalDateTime.now(),
                previousStatus,
                existingOrder.getStatus(),
                existingOrder.getEmployeeId(),
                null
        );
        traceabilityClientPort.createTrace(traceReq, authHeader);
        return existingOrder;
    }

    @Override
    public OrderModel getOrderById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(DomainConstants.ORDER_CANT_NULL);
        }
        OrderModel order = orderPersistencePort.getUserById(id);
        if (order == null) {
            throw new RuntimeException(DomainConstants.ORDER_NOT_FOUND + id);
        }
        return order;
    }
}
