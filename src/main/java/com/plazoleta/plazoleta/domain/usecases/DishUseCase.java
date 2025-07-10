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
import org.springframework.security.core.Authentication;  //Sacar esto de aca

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
        Long ownerId = claims.get(DomainConstants.OWNER_ID, Long.class);

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
    public void update(Long id, DishModel updatedFields) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long ownerId = ((Claims) auth.getPrincipal()).get(DomainConstants.OWNER_ID, Long.class);

        DishModel existing = dishPersistencePort.getById(id);
        if (existing == null) {
            throw new WrongArgumentException(DomainConstants.DISH_NOT_FOUND);

        }

        RestaurantModel rest = restaurantPersistencePort
                .findById(existing.getRestaurantId())
                .orElseThrow(() -> new WrongArgumentException(
                        DomainConstants.NON_EXISTING_RESTAURANT
                ));
        if (!rest.getOwnerId().equals(ownerId)) {
            throw new UnauthorizedAccessException(DomainConstants.UPDATE_DISH_NOT_ALLOWED);
        }

        existing.setPrice(updatedFields.getPrice());
        existing.setDescription(updatedFields.getDescription());

        dishPersistencePort.update(id, existing);
    }


    @Override
    public void updateStatus(Long id, DishModel dishModel) {
        Claims claims = (Claims) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Long ownerId = claims.get(DomainConstants.OWNER_ID, Long.class);

        DishModel existingDish = dishPersistencePort.getById(id);
        if (existingDish == null) {
            throw new WrongArgumentException(DomainConstants.DISH_NOT_FOUND);
        }

        RestaurantModel rest = restaurantPersistencePort
                .findById(existingDish.getRestaurantId())
                .orElseThrow(() -> new WrongArgumentException(
                        DomainConstants.NON_EXISTING_RESTAURANT
                ));
        if (!rest.getOwnerId().equals(ownerId)) {
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
