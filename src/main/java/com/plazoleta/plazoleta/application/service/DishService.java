package com.plazoleta.plazoleta.application.service;

import com.plazoleta.plazoleta.application.dto.request.SaveDishRequest;
import com.plazoleta.plazoleta.application.dto.request.UpdateDishRequest;
import com.plazoleta.plazoleta.application.dto.request.UpdateDishStatusRequest;
import com.plazoleta.plazoleta.application.dto.response.DishResponse;
import com.plazoleta.plazoleta.application.dto.response.RestaurantResponse;
import com.plazoleta.plazoleta.application.dto.response.SaveDishResponse;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;

public interface DishService {
    SaveDishResponse save(SaveDishRequest request);
    SaveDishResponse update(Long id, UpdateDishRequest request);
    SaveDishResponse updateDishStatus (Long id, UpdateDishStatusRequest request);
    PagedResult<DishResponse> getDishes (Long restaurantId,
                                         Integer page,
                                         Integer size,
                                         String name,
                                         Integer price,
                                         String description,
                                         String urlImage,
                                         String category,
                                         boolean active,
                                         String sortBy,
                                         boolean orderAsc);
}
