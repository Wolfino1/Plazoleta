package com.plazoleta.plazoleta.application.service.Impl;

import com.plazoleta.plazoleta.application.dto.request.SaveRestaurantRequest;
import com.plazoleta.plazoleta.application.dto.response.SaveRestaurantResponse;
import com.plazoleta.plazoleta.application.mappers.RestaurantDtoMapper;
import com.plazoleta.plazoleta.application.service.RestaurantService;
import com.plazoleta.plazoleta.common.configurations.util.Constants;
import com.plazoleta.plazoleta.domain.ports.in.RestaurantServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantServicePort restaurantServicePort;
    private final RestaurantDtoMapper restaurantDtoMapper;

    @Override
    public SaveRestaurantResponse save(SaveRestaurantRequest request) {
        restaurantServicePort.save(restaurantDtoMapper.requestToModel(request));
        return new SaveRestaurantResponse(Constants.SAVE_RESTAURANT_RESPONSE_MESSAGE, LocalDateTime.now());    }
}
