package com.plazoleta.plazoleta.application.service.Impl;

import com.plazoleta.plazoleta.application.dto.request.SaveOrderRequest;
import com.plazoleta.plazoleta.application.dto.response.OrderItemResponse;
import com.plazoleta.plazoleta.application.dto.response.OrderResponse;
import com.plazoleta.plazoleta.application.dto.response.SaveOrderResponse;
import com.plazoleta.plazoleta.application.mappers.OrderDtoMapper;
import com.plazoleta.plazoleta.application.service.OrderService;
import com.plazoleta.plazoleta.common.configurations.util.Constants;
import com.plazoleta.plazoleta.domain.models.DishModel;
import com.plazoleta.plazoleta.domain.models.OrderModel;
import com.plazoleta.plazoleta.domain.models.OrderStatus;
import com.plazoleta.plazoleta.domain.ports.in.OrderServicePort;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderServicePort orderServicePort;
    private final OrderDtoMapper orderDtoMapper;

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
                                    null,                    // dishName
                                    itemModel.getQuantity(),
                                    null                     // unitPrice
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


}
