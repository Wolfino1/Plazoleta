package com.plazoleta.plazoleta.domain.ports.out;

import com.plazoleta.plazoleta.domain.models.RestaurantModel;

public interface RestaurantPersistencePort {
    void save(RestaurantModel restaurantModel);

}
