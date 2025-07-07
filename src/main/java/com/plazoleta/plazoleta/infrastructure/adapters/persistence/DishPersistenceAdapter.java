package com.plazoleta.plazoleta.infrastructure.adapters.persistence;

import com.plazoleta.plazoleta.domain.models.DishModel;
import com.plazoleta.plazoleta.domain.ports.out.DishPersistencePort;
import com.plazoleta.plazoleta.domain.util.constants.DomainConstants;
import com.plazoleta.plazoleta.infrastructure.entities.DishEntity;
import com.plazoleta.plazoleta.infrastructure.mappers.DishEntityMapper;
import com.plazoleta.plazoleta.infrastructure.repositories.mysql.DishRepository;
import jakarta.persistence.EntityNotFoundException;
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
                        new IllegalArgumentException(DomainConstants.DISH_NOT_FOUND + id)
                );
        existingDish.setPrice(dishModel.getPrice());
        existingDish.setDescription(dishModel.getDescription());
        dishRepository.save(existingDish);
    }

    @Override
    public void updateStatus(Long id, DishModel dishModel) {
        DishEntity existingDish = dishRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(DomainConstants.DISH_NOT_FOUND + id));
        existingDish.setActive(dishModel.isActive());
        dishRepository.save(existingDish);
    }


    @Override
    public DishModel getById(Long id) {
        DishEntity dishEntity = dishRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(DomainConstants.DISH_NOT_FOUND + id));
        return dishEntityMapper.entityToModel(dishEntity);
    }
}
