package com.plazoleta.plazoleta.application.service.Impl;

import com.plazoleta.plazoleta.application.dto.request.*;
import com.plazoleta.plazoleta.application.dto.response.*;
import com.plazoleta.plazoleta.application.mappers.OrderDtoMapper;
import com.plazoleta.plazoleta.application.service.OrderService;
import com.plazoleta.plazoleta.common.configurations.util.Constants;
import com.plazoleta.plazoleta.domain.models.DishModel;
import com.plazoleta.plazoleta.domain.models.OrderModel;
import com.plazoleta.plazoleta.domain.models.OrderStatus;
import com.plazoleta.plazoleta.domain.ports.in.OrderServicePort;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderServicePort orderServicePort;
    private final OrderDtoMapper orderDtoMapper;
    private final HttpServletRequest httpRequest;

    @Override
    public SaveOrderResponse save(SaveOrderRequest request) {
        orderServicePort.save(orderDtoMapper.requestToModel(request));
        return new SaveOrderResponse(Constants.SAVE_ORDER_RESPONSE_MESSAGE, LocalDateTime.now());    }

    @Override
    public PagedResult<OrderResponse> getOrdersByFilter(
            Long restaurantId, int page, int size, Long clientId, OrderStatus status) {

        PagedResult<OrderModel> orderPage = orderServicePort.getOrdersByFilter(
                restaurantId, page, size, clientId, status
        );

        List<OrderResponse> content = orderPage.getContent().stream()
                .map(orderModel -> {
                    List<OrderItemResponse> items = orderModel.getItems().stream()
                            .map(itemModel -> new OrderItemResponse(
                                    itemModel.getDishId(),
                                    itemModel.getQuantity()
                            ))
                            .collect(Collectors.toList());

                    return new OrderResponse(
                            orderModel.getId(),
                            orderModel.getClientId(),
                            orderModel.getStatus(),
                            items
                    );
                })
                .collect(Collectors.toList());

        return new PagedResult<>(
                content,
                orderPage.getPage(),
                orderPage.getSize(),
                orderPage.getTotalElements()
        );
    }

    @Override
    public AssignEmployeeToOrderResponse assignEmployeeToOrder(
            Long id,
            AssignEmployeeToOrderRequest request
    ) {
        OrderModel updateFields = orderDtoMapper.assignEmployeeRequestToModel(request);

        OrderModel updated = orderServicePort.assignEmployee(id, updateFields);

        return orderDtoMapper.modelToAssignEmployeeResponse(updated);
    }

    @Override
    public ChangeOrderStatusResponse changeOrderStatus(Long id, ChangeOrderStatusRequest request) {
        OrderModel updateStatus = orderDtoMapper.changeOrderStatusRequestToModel(request);
        OrderModel updated = orderServicePort.changeStatus(id, updateStatus);
        return orderDtoMapper.modelToChangeOrderResponse(updated);
    }

    @Override
    public SaveCompleteOrderResponse completeOrderStatus(Long id, CompleteOrderRequest request) {
        Integer pin = request.pinSecurity();
        OrderModel completed = orderServicePort.completeOrder(id, pin);
        return orderDtoMapper.modelToSaveCompleteOrderResponse(completed);
    }

    @Override
    public CancelOrderResponse cancelOrder(Long id, CancelOrderRequest request) {
        OrderModel cancelOrder = orderDtoMapper.cancelOrder(request);
        OrderModel canceled = orderServicePort.cancelOrder (id, cancelOrder);
        return orderDtoMapper.modelToCancelOrderResponse (canceled);


    }
}
