package com.plazoleta.plazoleta.application.service.Impl;

import com.plazoleta.plazoleta.application.dto.request.SaveOrderRequest;
import com.plazoleta.plazoleta.application.dto.response.SaveOrderResponse;
import com.plazoleta.plazoleta.application.mappers.OrderDtoMapper;
import com.plazoleta.plazoleta.application.service.OrderService;
import com.plazoleta.plazoleta.common.configurations.util.Constants;
import com.plazoleta.plazoleta.domain.ports.in.OrderServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderServicePort orderServicePort;
    private final OrderDtoMapper orderDtoMapper;

    @Override
    public SaveOrderResponse save(SaveOrderRequest request) {
        orderServicePort.save(orderDtoMapper.requestToModel(request));
        return new SaveOrderResponse(Constants.SAVE_ORDER_RESPONSE_MESSAGE, LocalDateTime.now());    }
}
