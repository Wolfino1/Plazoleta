package com.plazoleta.plazoleta.application.service;

import com.plazoleta.plazoleta.application.dto.request.SaveDishRequest;
import com.plazoleta.plazoleta.application.dto.request.SaveOrderRequest;
import com.plazoleta.plazoleta.application.dto.response.SaveDishResponse;
import com.plazoleta.plazoleta.application.dto.response.SaveOrderResponse;

public interface OrderService {
    SaveOrderResponse save(SaveOrderRequest request);

}
