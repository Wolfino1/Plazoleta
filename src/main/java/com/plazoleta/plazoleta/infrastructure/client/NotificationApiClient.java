package com.plazoleta.plazoleta.infrastructure.client;

import com.plazoleta.plazoleta.domain.ports.out.NotificationClientPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class NotificationApiClient implements NotificationClientPort {

    private final WebClient webClient;

    public NotificationApiClient(
            @Qualifier("messagingWebClient") WebClient webClient
    ) {
        this.webClient = webClient;
    }

    @Override
    public void notifyOrderReady(Long orderId,
                                 Long userId,
                                 String status,
                                 Integer pinSecurity,
                                 String phoneNumber) {
        var req = Map.of(
                "orderId", orderId,
                "userId", userId,
                "status", status,
                "pinSecurity", pinSecurity,
                "phoneNumber", phoneNumber
        );
        webClient.post()
                .uri("")                  // ya usa base-url + “/”
                .bodyValue(req)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
