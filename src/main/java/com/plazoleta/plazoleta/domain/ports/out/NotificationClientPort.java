package com.plazoleta.plazoleta.domain.ports.out;

public interface NotificationClientPort {

    void notifyOrderReady(
            Long orderId,
            Long userId,
            String status,
            Integer pinSecurity,
            String phoneNumber
    );

}
