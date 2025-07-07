package com.plazoleta.plazoleta.domain.ports.in;

import com.plazoleta.plazoleta.application.dto.response.DishResponse;
import com.plazoleta.plazoleta.domain.models.DishModel;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;

public interface DishServicePort {
    void save (DishModel dishModel);
    void update(Long id, DishModel dishModel);
    void updateStatus(Long id, DishModel dishModel);
    PagedResult<DishModel> getDishes (Long restaurantId,
                                         Integer page,
                                         Integer size,
                                         String name,
                                         Integer price,
                                         String description,
                                         String urlImage,
                                         String category,
                                         boolean active,
                                         String sortBy,
                                         boolean orderAsc);
}
