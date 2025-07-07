package com.plazoleta.plazoleta.domain.models;

public class OrderItemModel {
    private Long dishId;
    private Integer quantity;

    public OrderItemModel(Long dishId, Integer quantity) {
        this.dishId = dishId;
        this.quantity = quantity;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
