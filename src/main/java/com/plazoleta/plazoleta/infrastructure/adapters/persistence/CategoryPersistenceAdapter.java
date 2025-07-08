package com.plazoleta.plazoleta.infrastructure.adapters.persistence;

import com.plazoleta.plazoleta.domain.models.CategoryModel;
import com.plazoleta.plazoleta.domain.ports.out.CategoryPersistencePort;
import com.plazoleta.plazoleta.infrastructure.entities.CategoryEntity;
import com.plazoleta.plazoleta.infrastructure.mappers.CategoryEntityMapper;
import com.plazoleta.plazoleta.infrastructure.repositories.mysql.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements CategoryPersistencePort {

    private final CategoryRepository categoryRepository;
    private final CategoryEntityMapper categoryEntityMapper;

    @Override
    public Optional<CategoryModel> getById(Long id) {
        Optional<CategoryEntity> roleEntity = categoryRepository.findById(id);
        return roleEntity.map(categoryEntityMapper::entityToModel);
    }
}
