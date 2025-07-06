package com.plazoleta.plazoleta.domain.usecases;

import com.plazoleta.plazoleta.domain.models.DishModel;
import com.plazoleta.plazoleta.domain.ports.in.DishServicePort;
import com.plazoleta.plazoleta.domain.ports.out.DishPersistencePort;

public class DishUseCase implements DishServicePort {
    private final DishPersistencePort dishPersistencePort;

    public DishUseCase(DishPersistencePort dishPersistencePort) {
        this.dishPersistencePort = dishPersistencePort;
    }

    @Override
    public void save(DishModel dishModel) {
        dishPersistencePort.save(dishModel);
    }
}
