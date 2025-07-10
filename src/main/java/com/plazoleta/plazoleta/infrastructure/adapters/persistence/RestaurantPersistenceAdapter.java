package com.plazoleta.plazoleta.infrastructure.adapters.persistence;

import com.plazoleta.plazoleta.domain.models.RestaurantModel;
import com.plazoleta.plazoleta.domain.ports.out.RestaurantPersistencePort;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;
import com.plazoleta.plazoleta.infrastructure.entities.RestaurantEntity;
import com.plazoleta.plazoleta.infrastructure.mappers.RestaurantEntityMapper;
import com.plazoleta.plazoleta.infrastructure.repositories.mysql.RestaurantRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

        Sort sort = orderAsc
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<RestaurantEntity> pageEntities =
                restaurantRepository.findWithFilters(id, name, logoUrl, ownerId, pageable);

        List<RestaurantModel> content = pageEntities.getContent().stream()
                .map(restaurantEntityMapper::entityToModel)
                .toList();

        return new PagedResult<>(
                content,
                pageEntities.getNumber(),
                pageEntities.getSize(),
                pageEntities.getTotalElements()
        );
    }

    @Override
    public Optional<RestaurantModel> findById(Long id) {
        return restaurantRepository.findById(id)
                .map(restaurantEntityMapper::entityToModel);
    }

}
