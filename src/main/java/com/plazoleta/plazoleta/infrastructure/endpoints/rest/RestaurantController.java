package com.plazoleta.plazoleta.infrastructure.endpoints.rest;

import com.plazoleta.plazoleta.application.dto.request.SaveRestaurantRequest;
import com.plazoleta.plazoleta.application.dto.response.*;
import com.plazoleta.plazoleta.application.service.RestaurantService;
import com.plazoleta.plazoleta.domain.models.RestaurantModel;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/restaurant")
@RequiredArgsConstructor
@Tag(name= "Restaurant", description = "Controller for restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    @Operation(summary = "Create restaurant", description = "This method saves a restaurant", tags =
            {"Restaurant"}, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description =
            "Creates a new Restaurant", required = true, content = @Content(mediaType = "application/jason",
            schema = @Schema (implementation = SaveRestaurantRequest.class))), responses = {@ApiResponse(
            responseCode = "200",
            description = "Restaurant created successfully",
            content = @Content(mediaType = "application/jason",
                    schema = @Schema (implementation = SaveRestaurantResponse.class))
    )})
    public ResponseEntity<SaveRestaurantResponse> save(@RequestBody SaveRestaurantRequest saveRestaurantRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.save(saveRestaurantRequest));
    }

    @GetMapping("/get")
    public ResponseEntity<PagedResult<RestaurantResponse>> getRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) String logoUrl,
            @RequestParam(required = false, defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "true") boolean orderAsc) {

        return ResponseEntity.ok(restaurantService.getRestaurants(
                page, size, id, name, ownerId ,logoUrl, sortBy ,orderAsc));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantByIdResponse> getById(@PathVariable Long id) {

        RestaurantModel restaurant = restaurantService.getRestaurantById(id);
        RestaurantByIdResponse resp = new RestaurantByIdResponse(restaurant.getId(), restaurant.getOwnerId());
        return ResponseEntity.ok(resp);
    }
}