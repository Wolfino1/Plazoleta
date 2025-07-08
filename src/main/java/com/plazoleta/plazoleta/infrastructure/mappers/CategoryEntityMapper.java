package com.plazoleta.plazoleta.infrastructure.mappers;

import com.plazoleta.plazoleta.domain.models.CategoryModel;
import com.plazoleta.plazoleta.infrastructure.entities.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface CategoryEntityMapper {
    CategoryModel entityToModel (CategoryEntity categoryEntity);
}
