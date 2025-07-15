package com.plazoleta.plazoleta.infrastructure.endpoints.rest;

import com.plazoleta.plazoleta.application.dto.request.*;
import com.plazoleta.plazoleta.application.dto.response.*;
import com.plazoleta.plazoleta.application.service.OrderService;
import com.plazoleta.plazoleta.domain.models.OrderModel;
import com.plazoleta.plazoleta.domain.models.OrderStatus;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestHeader;



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
            Authentication authentication
    ) {
        Claims claims = (Claims) authentication.getPrincipal();

        Integer clientIdInt = claims.get("clientId", Integer.class);
        Long clientId = clientIdInt.longValue();

        SaveOrderRequest reqWithClient = new SaveOrderRequest(
                clientId,
                saveOrderRequest.restaurantId(),
                saveOrderRequest.phoneNumber(),
                saveOrderRequest.items()

        );

        SaveOrderResponse response = orderService.save(reqWithClient);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/restaurants/{restaurantId}/orders")
    public ResponseEntity<PagedResult<OrderResponse>> getOrdersByFilter(@PathVariable Long restaurantId,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size,
                                                                        @RequestParam(required = false) Long clientId,
                                                                        @RequestParam(required = false) OrderStatus status) {
        return ResponseEntity.ok(orderService.getOrdersByFilter(restaurantId, page, size, clientId, status));
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PatchMapping("/{id}/assign")
    public ResponseEntity<AssignEmployeeToOrderResponse> assignEmployeeToOrder(
            @PathVariable Long id,
            @RequestBody AssignEmployeeToOrderRequest request,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
    ) {
        AssignEmployeeToOrderResponse response = orderService.assignEmployeeToOrder(id, request, authHeader);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ChangeOrderStatusResponse> changeOrderStatus(
            @PathVariable Long id,
            @RequestBody ChangeOrderStatusRequest request,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader

    ) {
        ChangeOrderStatusResponse response = orderService.changeOrderStatus(id, request, authHeader);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PatchMapping("/{id}/complete")
    public ResponseEntity<SaveCompleteOrderResponse> completeOrder(
            @PathVariable Long id,
            @RequestBody CompleteOrderRequest request,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader

    ) {
        SaveCompleteOrderResponse response = orderService.completeOrderStatus(id, request, authHeader);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<CancelOrderResponse> cancelOrder(
            @PathVariable Long id,
            CancelOrderRequest request,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader

    ) {
        CancelOrderResponse response = orderService.cancelOrder(id, request, authHeader);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderByIdResponse> getById(@PathVariable Long id) {

        OrderModel order = orderService.getOrderById(id);
        OrderByIdResponse resp = new OrderByIdResponse(order.getId(), order.getClientId(),
                order.getStatus(), order.getEmployeeId());
        return ResponseEntity.ok(resp);
    }
}


