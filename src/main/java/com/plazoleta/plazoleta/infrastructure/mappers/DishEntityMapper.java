package com.plazoleta.plazoleta.infrastructure.mappers;

import com.plazoleta.plazoleta.domain.models.DishModel;
import com.plazoleta.plazoleta.infrastructure.entities.CategoryEntity;
import com.plazoleta.plazoleta.infrastructure.entities.DishEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DishEntityMapper {

    @Mapping(target = "restaurant", ignore = true)
    @Mapping(target = "category", source = "category")
    DishEntity modelToEntity(DishModel dishModel);

    @Mapping(source = "restaurant.id", target = "restaurantId")
    @Mapping(source = "category.id", target = "category")
    DishModel entityToModel(DishEntity dishEntity);

    default CategoryEntity map(Long categoryId) {
        if (categoryId == null) return null;
        CategoryEntity category = new CategoryEntity();
        category.setId(categoryId);
        return category;
    }

    default Long map(CategoryEntity categoryEntity) {
        if (categoryEntity == null) return null;
        return categoryEntity.getId();
    }
}

