package com.plazoleta.plazoleta.domain.models;

import com.plazoleta.plazoleta.domain.exceptions.EmptyException;
import com.plazoleta.plazoleta.domain.exceptions.NullException;
import com.plazoleta.plazoleta.domain.exceptions.WrongArgumentException;
import com.plazoleta.plazoleta.domain.util.constants.DomainConstants;

public class DishModel {
    private Long id;
    private String name;
    private Integer price;
    private String description;
    private String urlImage;
    private Long  idCategory;
    private Long restaurantId;
    boolean active = true;

    public DishModel(String name, Integer price, String description, String urlImage, Long  idCategory,
                     Long restaurantId) {
        setName(name);
        setPrice(price);
        setDescription(description);
        setUrlImage(urlImage);
        setCategory(idCategory);
        setRestaurantId(restaurantId);
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
            throw new WrongArgumentException(DomainConstants.WRONG_ARGUMENT_PRICE);
        }
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.isEmpty()) {
            throw new WrongArgumentException(DomainConstants.WRONG_ARGUMENT_DESCRIPTION);
        }
        this.description = description;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        if (urlImage == null || urlImage.isEmpty()) {
            throw new WrongArgumentException(DomainConstants.WRONG_ARGUMENT_URLIMAGE);
        }
        this.urlImage = urlImage;
    }

    public long getCategory() {
        if (idCategory == null) {
            throw new WrongArgumentException(DomainConstants.ID_CATEGORY_MISSING);
        }
        return idCategory;
    }

    public void setCategory(Long idCategory) {
        if (idCategory == null || idCategory <= 0) {
            throw new WrongArgumentException(DomainConstants.WRONG_ARGUMENT_CATEGORY);
        }
        this.idCategory = idCategory;
    }


    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        if (restaurantId == null|| restaurantId <= 0) {
            throw new WrongArgumentException(DomainConstants.WRONG_ARGUMENT_RESTAURANT);
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
