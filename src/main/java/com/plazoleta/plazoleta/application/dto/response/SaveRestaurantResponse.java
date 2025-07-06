package com.plazoleta.plazoleta.application.dto.response;

import java.time.LocalDateTime;

public record SaveRestaurantResponse (String message, LocalDateTime time) {
}