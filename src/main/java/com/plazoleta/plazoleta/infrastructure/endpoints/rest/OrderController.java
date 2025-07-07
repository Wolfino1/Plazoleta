package com.plazoleta.plazoleta.infrastructure.endpoints.rest;
import com.plazoleta.plazoleta.application.dto.request.SaveOrderRequest;
import com.plazoleta.plazoleta.application.dto.response.SaveOrderResponse;
import com.plazoleta.plazoleta.application.service.OrderService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;


@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Controller for orders")
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/")
    public ResponseEntity<SaveOrderResponse> save(
            @RequestBody @Valid SaveOrderRequest saveOrderRequest,
            Authentication authentication    // <— inyecta la auth genérica
    ) {
        // 1) Obtén los claims que pusiste como principal en tu filtro
        Claims claims = (Claims) authentication.getPrincipal();

        // 2) Lee el clientId (puede venir como Integer)
        Integer clientIdInt = claims.get("clientId", Integer.class);
        Long clientId = clientIdInt.longValue();

        // 3) Reconstruye el request forzando el clientId
        SaveOrderRequest reqWithClient = new SaveOrderRequest(
                clientId,
                saveOrderRequest.restaurantId(),
                saveOrderRequest.items()
        );

        // 4) Llama al servicio
        SaveOrderResponse response = orderService.save(reqWithClient);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

