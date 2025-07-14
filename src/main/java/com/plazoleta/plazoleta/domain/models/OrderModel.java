package com.plazoleta.plazoleta.domain.models;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class OrderModel {
    private Long id;
    private Long clientId;
    private Long restaurantId;
    private OrderStatus status;
    private List<OrderItemModel> items;
    private Long employeeId;
    private Integer pinSecurity;
    private String phoneNumber;

    public OrderModel(Long id, Long clientId, Long restaurantId, OrderStatus status, List<OrderItemModel> items,
                      Long employeeId, Integer pinSecurity, String phoneNumber) {
        setId(id);
        setClientId(clientId);
        setRestaurantId(restaurantId);
        setStatus(status);
        setItems(items);
        this.employeeId = employeeId;
        this.pinSecurity = pinSecurity;
        setPhoneNumber(phoneNumber);
    }

    public Integer getPinSecurity() {
        return pinSecurity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItemModel> getItems() {
        return items;
    }

    public void setItems(List<OrderItemModel> items) {
        this.items = items;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}


