package com.plazoleta.plazoleta.domain.ports.out;

import com.plazoleta.plazoleta.application.dto.response.RestaurantResponse;
import com.plazoleta.plazoleta.domain.models.RestaurantModel;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;

public interface RestaurantPersistencePort {
    void save(RestaurantModel restaurantModel);
    PagedResult<RestaurantModel> getRestaurants (Integer page, Integer size, String name,
                                                    String logoUrl, String sortBy, boolean orderAsc);
}
