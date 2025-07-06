package com.plazoleta.plazoleta.domain.ports.in;

import com.plazoleta.plazoleta.domain.models.RestaurantModel;

public interface RestaurantServicePort {
    void save(RestaurantModel restaurantModel);
}
