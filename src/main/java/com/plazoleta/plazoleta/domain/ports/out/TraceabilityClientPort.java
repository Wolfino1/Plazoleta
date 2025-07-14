package com.plazoleta.plazoleta.domain.ports.out;

import com.plazoleta.plazoleta.infrastructure.client.dto.CreateTraceabilityRequest;

public interface TraceabilityClientPort {
    void createTrace(CreateTraceabilityRequest request, String authHeader);

}
