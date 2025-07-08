package com.plazoleta.plazoleta.application.dto.request;

public record SaveRestaurantRequest(String name, String nit, String address, String phoneNumber,
                                    String logoUrl, Long ownerId) {
}
