package com.plazoleta.plazoleta.infrastructure.client;


import com.plazoleta.plazoleta.domain.ports.out.TraceabilityClientPort;
import com.plazoleta.plazoleta.infrastructure.client.dto.CreateTraceabilityRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class TraceabilityClientAdapter implements TraceabilityClientPort {
    private static final Logger log = LoggerFactory.getLogger(TraceabilityClientAdapter.class);
    @Qualifier("traceabilityWebClient")
    private final WebClient traceabilityWebClient;

    @Override
    public void createTrace(CreateTraceabilityRequest request, String authHeader) {

        traceabilityWebClient
                .post()
                .uri("/trazabilidad")
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
