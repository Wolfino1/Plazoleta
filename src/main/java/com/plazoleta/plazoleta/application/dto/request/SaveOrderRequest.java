package com.plazoleta.plazoleta.application.dto.request;

import java.util.List;

public record SaveOrderRequest(Long clientId,
                               Long restaurantId,
                               List<SaveOrderItem> items) {
}
