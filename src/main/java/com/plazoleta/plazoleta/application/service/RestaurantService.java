package com.plazoleta.plazoleta.application.service;

import com.plazoleta.plazoleta.application.dto.request.SaveRestaurantRequest;
import com.plazoleta.plazoleta.application.dto.response.RestaurantResponse;
import com.plazoleta.plazoleta.application.dto.response.SaveRestaurantResponse;
import com.plazoleta.plazoleta.domain.models.RestaurantModel;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;

public interface RestaurantService {
    SaveRestaurantResponse save(SaveRestaurantRequest request);
    PagedResult<RestaurantResponse> getRestaurants (Integer page, Integer size, Long id, String name,
                                                    Long ownerId, String logoUrl, String sortBy, boolean orderAsc);
    RestaurantModel getRestaurantById(Long id);
}
