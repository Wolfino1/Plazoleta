package com.plazoleta.plazoleta.domain.models;

import com.plazoleta.plazoleta.domain.exceptions.EmptyException;
import com.plazoleta.plazoleta.domain.exceptions.NullException;
import com.plazoleta.plazoleta.domain.util.constants.DomainConstants;

public class DishModel {
    Long id;
    String name;
    Integer price;
    String description;
    String urlImage;
    String category; //Tabla aparte
    Long restaurantId;
    boolean active = true;

    public DishModel(String name, Integer price, String description, String urlImage, String category,
                     Long restaurantId) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.urlImage = urlImage;
        this.category = category;
        this.restaurantId = restaurantId;
    }

    public DishModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) throw new NullException(DomainConstants.NAME_NULL_MESSAGE);
        if (name.trim().isEmpty()) throw new EmptyException(DomainConstants.NAME_EMPTY_MESSAGE);
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        if (price == null || price <= 0) {
            throw new IllegalArgumentException(DomainConstants.WRONG_ARGUMENT_PRICE);
        }
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException(DomainConstants.WRONG_ARGUMENT_DESCRIPTION);
        }
        this.description = description;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        if (urlImage == null || urlImage.isEmpty()) {
            throw new IllegalArgumentException(DomainConstants.WRONG_ARGUMENT_URLIMAGE);
        }
        this.urlImage = urlImage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException(DomainConstants.WRONG_ARGUMENT_CATEGORY);
        }
        this.category = category;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        if (restaurantId == null) {
            throw new IllegalArgumentException(DomainConstants.WRONG_ARGUMENT_RESTAURANT);
        }
        this.restaurantId = restaurantId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
