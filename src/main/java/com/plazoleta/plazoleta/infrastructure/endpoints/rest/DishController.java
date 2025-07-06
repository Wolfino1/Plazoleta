package com.plazoleta.plazoleta.infrastructure.endpoints.rest;

import com.plazoleta.plazoleta.application.dto.request.SaveDishRequest;
import com.plazoleta.plazoleta.application.dto.request.UpdateDishRequest;
import com.plazoleta.plazoleta.application.dto.response.SaveDishResponse;
import com.plazoleta.plazoleta.application.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
@RequestMapping("/api/v1/dish")
@RequiredArgsConstructor
@Tag(name = "Dish", description = "Controller for dishes")
public class DishController {

    private final DishService dishService;

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/")
    @Operation(
            summary = "Create dish",
            description = "This method saves a dish",
            tags = {"Dish"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Creates a new Dish",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SaveDishRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Dish created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SaveDishResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SaveDishResponse> save(@RequestBody SaveDishRequest saveDishRequest) {
        SaveDishResponse response = dishService.save(saveDishRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('OWNER')")
    @PutMapping("/{id}")
    @Operation(
            summary = "Update dish",
            description = "This method updates price and description of an existing dish",
            tags = {"Dish"},
            parameters = {
                    @Parameter(
                            name = "id",
                            in = ParameterIn.PATH,
                            description = "ID of the dish to update",
                            required = true
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Fields to update for the Dish",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateDishRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Dish updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SaveDishResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SaveDishResponse> update(
            @PathVariable Long id,
            @RequestBody UpdateDishRequest updateDishRequest
    ) {
        SaveDishResponse response = dishService.update(id, updateDishRequest);
        return ResponseEntity.ok(response);
    }
}

