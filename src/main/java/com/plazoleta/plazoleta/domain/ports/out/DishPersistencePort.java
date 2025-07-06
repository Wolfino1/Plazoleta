package com.plazoleta.plazoleta.domain.ports.out;

import com.plazoleta.plazoleta.domain.models.DishModel;

public interface DishPersistencePort {
    void save (DishModel dishModel);
}
