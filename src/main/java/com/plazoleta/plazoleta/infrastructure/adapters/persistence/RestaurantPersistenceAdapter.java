package com.plazoleta.plazoleta.infrastructure.adapters.persistence;

import com.plazoleta.plazoleta.domain.models.RestaurantModel;
import com.plazoleta.plazoleta.domain.ports.out.RestaurantPersistencePort;
import com.plazoleta.plazoleta.infrastructure.mappers.RestaurantEntityMapper;
import com.plazoleta.plazoleta.infrastructure.repositories.mysql.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RestaurantPersistenceAdapter implements RestaurantPersistencePort {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantEntityMapper restaurantEntityMapper;

    @Override
    public void save(RestaurantModel restaurantModel) {
        restaurantRepository.save(restaurantEntityMapper.modelToEntity(restaurantModel));
    }
}
