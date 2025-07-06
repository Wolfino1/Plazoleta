package com.plazoleta.plazoleta.application.dto.response;

import java.time.LocalDateTime;

public record SaveDishResponse (String message, LocalDateTime time) {
}