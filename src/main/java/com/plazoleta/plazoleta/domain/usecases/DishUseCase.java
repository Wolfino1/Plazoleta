package com.plazoleta.plazoleta.domain.usecases;

import com.plazoleta.plazoleta.domain.exceptions.UnauthorizedAccessException;
import com.plazoleta.plazoleta.domain.exceptions.WrongArgumentException;
import com.plazoleta.plazoleta.domain.models.DishModel;
import com.plazoleta.plazoleta.domain.models.RestaurantModel;
import com.plazoleta.plazoleta.domain.ports.in.DishServicePort;
import com.plazoleta.plazoleta.domain.ports.out.DishPersistencePort;
import com.plazoleta.plazoleta.domain.ports.out.RestaurantPersistencePort;
import com.plazoleta.plazoleta.domain.util.constants.DomainConstants;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

public class DishUseCase implements DishServicePort {
    private final DishPersistencePort dishPersistencePort;
    private final RestaurantPersistencePort restaurantPersistencePort;

    public DishUseCase(DishPersistencePort dishPersistencePort, RestaurantPersistencePort restaurantPersistencePort) {
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
    }


    @Override
    public void save(DishModel dishModel) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Claims claims = (Claims) auth.getPrincipal();
        Long ownerId = claims.get("ownerId", Long.class);

        RestaurantModel restaurant = restaurantPersistencePort
                .findById(dishModel.getRestaurantId())
                .orElseThrow(() -> new WrongArgumentException(
                        DomainConstants.NON_EXISTING_RESTAURANT
                ));

        if (!restaurant.getOwnerId().equals(ownerId)) {
            throw new UnauthorizedAccessException(DomainConstants.CREATE_DISH_NOT_ALLOWED);
        }

        dishPersistencePort.save(dishModel);
    }

    @Override
    public void update(Long id, DishModel dishModel) {
        dishPersistencePort.update(id, dishModel);
    }

    @Override
    public void updateStatus(Long id, DishModel dishModel) {
        Claims claims = (Claims) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Long userId = claims.get("ownerId", Long.class);

        DishModel existingDish = dishPersistencePort.getById(id);

        if (!existingDish.getRestaurantId().equals(userId)) {
            throw new UnauthorizedAccessException(DomainConstants.CHANGE_DISH_NOT_ALLOWED);
        }

        existingDish.setActive(dishModel.isActive());
        dishPersistencePort.updateStatus(id, existingDish);
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
                Long idCategory,
                boolean active,
                String sortBy,
                boolean orderAsc) {

            return dishPersistencePort.getDishes(
                    restaurantId,
                    page,
                    size,
                    name,
                    price,
                    description,
                    urlImage,
                    idCategory,
                    active,
                    sortBy,
                    orderAsc
            );
        }
    }
