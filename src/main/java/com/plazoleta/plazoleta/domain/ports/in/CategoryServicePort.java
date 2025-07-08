package com.plazoleta.plazoleta.domain.ports.in;

import com.plazoleta.plazoleta.domain.models.CategoryModel;

import java.util.Optional;

public interface CategoryServicePort {
    Optional<CategoryModel> getById(Long id);
}
