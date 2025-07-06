package com.plazoleta.plazoleta.application.service;

import com.plazoleta.plazoleta.application.dto.request.SaveDishRequest;
import com.plazoleta.plazoleta.application.dto.response.SaveDishResponse;

public interface DishService {
    SaveDishResponse save(SaveDishRequest request);
}
