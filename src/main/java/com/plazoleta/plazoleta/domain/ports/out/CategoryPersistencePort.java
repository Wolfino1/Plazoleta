package com.plazoleta.plazoleta.domain.ports.out;

import com.plazoleta.plazoleta.domain.models.CategoryModel;

import java.util.Optional;

public interface CategoryPersistencePort {

    Optional<CategoryModel> getById(Long id);
}
