package com.plazoleta.plazoleta.domain.usecases;

import com.plazoleta.plazoleta.domain.models.RestaurantModel;
import com.plazoleta.plazoleta.domain.ports.in.RestaurantServicePort;
import com.plazoleta.plazoleta.domain.ports.out.RestaurantPersistencePort;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;

public class RestaurantUseCase implements RestaurantServicePort {
    private final RestaurantPersistencePort restaurantPersistencePort;

    public RestaurantUseCase(RestaurantPersistencePort restaurantPersistencePort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public void save(RestaurantModel restaurantModel) {
        restaurantPersistencePort.save(restaurantModel);
    }

    @Override
    public PagedResult<RestaurantModel> getRestaurants(
            Integer page,
            Integer size,
            Long id,
            String name,
            Long ownerId,
            String logoUrl,
            String sortBy,
            boolean orderAsc) {
        return restaurantPersistencePort.getRestaurants(
                page,
                size,
                id,
                name,
                ownerId,
                logoUrl,
                sortBy,
                orderAsc
        );
    }
}
