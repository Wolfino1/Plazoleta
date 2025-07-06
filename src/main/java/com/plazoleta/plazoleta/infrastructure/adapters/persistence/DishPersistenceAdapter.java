package com.plazoleta.plazoleta.infrastructure.adapters.persistence;

import com.plazoleta.plazoleta.domain.models.DishModel;
import com.plazoleta.plazoleta.domain.ports.out.DishPersistencePort;
import com.plazoleta.plazoleta.infrastructure.entities.DishEntity;
import com.plazoleta.plazoleta.infrastructure.mappers.DishEntityMapper;
import com.plazoleta.plazoleta.infrastructure.repositories.mysql.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DishPersistenceAdapter implements DishPersistencePort {

    private final DishRepository dishRepository;
    private final DishEntityMapper dishEntityMapper;

    @Override
    public void save(DishModel dishModel) {
        DishEntity dishEntity = dishEntityMapper.modelToEntity(dishModel);
        dishRepository.save(dishEntity);
    }

    @Override
    public void update(Long id, DishModel dishModel) {
        DishEntity existingDish = dishRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Dish not found with id " + id)
                );
        // Solo actualizamos precio y descripción
        existingDish.setPrice(dishModel.getPrice());
        existingDish.setDescription(dishModel.getDescription());
        // Persistimos los cambios
        dishRepository.save(existingDish);
    }
}
