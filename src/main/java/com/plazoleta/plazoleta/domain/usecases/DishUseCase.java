package com.plazoleta.plazoleta.domain.usecases;

import com.plazoleta.plazoleta.domain.exceptions.UnauthorizedAccessException;
import com.plazoleta.plazoleta.domain.models.DishModel;
import com.plazoleta.plazoleta.domain.ports.in.DishServicePort;
import com.plazoleta.plazoleta.domain.ports.out.DishPersistencePort;
import com.plazoleta.plazoleta.domain.util.constants.DomainConstants;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.context.SecurityContextHolder;

public class DishUseCase implements DishServicePort {
    private final DishPersistencePort dishPersistencePort;

    public DishUseCase(DishPersistencePort dishPersistencePort) {
        this.dishPersistencePort = dishPersistencePort;
    }

    @Override
    public void save(DishModel dishModel) {
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
}
