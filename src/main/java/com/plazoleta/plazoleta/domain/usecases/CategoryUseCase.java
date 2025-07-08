package com.plazoleta.plazoleta.domain.usecases;

import com.plazoleta.plazoleta.domain.models.CategoryModel;
import com.plazoleta.plazoleta.domain.ports.in.CategoryServicePort;
import com.plazoleta.plazoleta.domain.ports.out.CategoryPersistencePort;

import java.util.Optional;

public class CategoryUseCase implements CategoryServicePort {

    private final CategoryPersistencePort categoryPersistencePort;

    public CategoryUseCase(CategoryPersistencePort categoryPersistencePort) {
        this.categoryPersistencePort = categoryPersistencePort;
    }

    @Override
    public Optional<CategoryModel> getById(Long id) {
        return categoryPersistencePort.getById(id);
    }
}
