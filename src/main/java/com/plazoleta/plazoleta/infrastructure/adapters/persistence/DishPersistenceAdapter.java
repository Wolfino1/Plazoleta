package com.plazoleta.plazoleta.infrastructure.adapters.persistence;

import com.plazoleta.plazoleta.domain.models.DishModel;
import com.plazoleta.plazoleta.domain.ports.out.DishPersistencePort;
import com.plazoleta.plazoleta.domain.util.constants.DomainConstants;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;
import com.plazoleta.plazoleta.infrastructure.entities.DishEntity;
import com.plazoleta.plazoleta.infrastructure.entities.RestaurantEntity;
import com.plazoleta.plazoleta.infrastructure.mappers.DishEntityMapper;
import com.plazoleta.plazoleta.infrastructure.repositories.mysql.DishRepository;
import com.plazoleta.plazoleta.infrastructure.repositories.mysql.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DishPersistenceAdapter implements DishPersistencePort {

    private final DishRepository dishRepository;
    private final DishEntityMapper dishEntityMapper;
    private final RestaurantRepository restaurantRepository;

    @Override
    public void save(DishModel dishModel) {
        DishEntity dishEntity = dishEntityMapper.modelToEntity(dishModel);

        RestaurantEntity restaurant = restaurantRepository
                .findById(dishModel.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No existe restaurante con id=" + dishModel.getRestaurantId()));
        dishEntity.setRestaurant(restaurant);

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

    @Override
    public PagedResult<DishModel> getDishes(
            Long restaurantId,
            Integer page,
            Integer size,
            String name,
            Integer price,
            String description,
            String urlImage,
            String category,
            boolean active,
            String sortBy,
            boolean orderAsc) {

        Sort sort = orderAsc
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<DishEntity> pageEntities = dishRepository.findWithFilters(
                restaurantId,
                name,
                price,
                description,
                urlImage,
                category,
                active,
                pageable
        );

        List<DishModel> content = pageEntities.getContent().stream()
                .map(dishEntityMapper::entityToModel)
                .toList();

        return new PagedResult<>(
                content,
                pageEntities.getNumber(),
                pageEntities.getSize(),
                pageEntities.getTotalElements()
        );
    }

}
