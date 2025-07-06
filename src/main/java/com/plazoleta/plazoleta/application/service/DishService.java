package com.plazoleta.plazoleta.application.service;

import com.plazoleta.plazoleta.application.dto.request.SaveDishRequest;
import com.plazoleta.plazoleta.application.dto.request.UpdateDishRequest;
import com.plazoleta.plazoleta.application.dto.response.SaveDishResponse;

public interface DishService {
    SaveDishResponse save(SaveDishRequest request);
    SaveDishResponse update(Long id, UpdateDishRequest request);

}
