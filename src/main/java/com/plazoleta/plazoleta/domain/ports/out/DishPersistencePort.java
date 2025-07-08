package com.plazoleta.plazoleta.domain.ports.out;

import com.plazoleta.plazoleta.domain.models.DishModel;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;

public interface DishPersistencePort {
    void save (DishModel dishModel);
    void update(Long id, DishModel dishModel);
    void updateStatus(Long id, DishModel dishModel);
    DishModel getById(Long id);
    PagedResult<DishModel> getDishes (Long restaurantId,
                                      Integer page,
                                      Integer size,
                                      String name,
                                      Integer price,
                                      String description,
                                      String urlImage,
                                      Long idCategory,
                                      boolean active,
                                      String sortBy,
                                      boolean orderAsc);
}
