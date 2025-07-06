package com.plazoleta.plazoleta.domain.ports.in;

import com.plazoleta.plazoleta.domain.models.DishModel;

public interface DishServicePort {
    void save (DishModel dishModel);
}
