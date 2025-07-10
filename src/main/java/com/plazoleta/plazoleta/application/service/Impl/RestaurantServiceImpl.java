package com.plazoleta.plazoleta.application.service.Impl;

import com.plazoleta.plazoleta.application.dto.request.SaveRestaurantRequest;
import com.plazoleta.plazoleta.application.dto.response.RestaurantResponse;
import com.plazoleta.plazoleta.application.dto.response.SaveRestaurantResponse;
import com.plazoleta.plazoleta.application.mappers.PageMapperApplication;
import com.plazoleta.plazoleta.application.mappers.RestaurantDtoMapper;
import com.plazoleta.plazoleta.application.service.RestaurantService;
import com.plazoleta.plazoleta.common.configurations.util.Constants;
import com.plazoleta.plazoleta.domain.models.RestaurantModel;
import com.plazoleta.plazoleta.domain.ports.in.RestaurantServicePort;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantServicePort restaurantServicePort;
    private final RestaurantDtoMapper restaurantDtoMapper;
    private final PageMapperApplication pageMapper;


    @Override
    public SaveRestaurantResponse save(SaveRestaurantRequest request) {
        restaurantServicePort.save(restaurantDtoMapper.requestToModel(request));
        return new SaveRestaurantResponse(Constants.SAVE_RESTAURANT_RESPONSE_MESSAGE, LocalDateTime.now());
    }

    @Override
    public PagedResult<RestaurantResponse> getRestaurants(
            Integer page,
            Integer size,
            Long id,
            String name,
            Long ownerId,
            String logoUrl,
            String sortBy,
            boolean orderAsc) {

        PagedResult<RestaurantModel> modelPage = restaurantServicePort.getRestaurants(
                page,
                size,
                id,
                name,
                ownerId,
                logoUrl,
                sortBy,
                orderAsc
        );

        List<RestaurantResponse> content = modelPage.getContent().stream()
                .map(r -> new RestaurantResponse(r.getId(),r.getName(), r.getLogoUrl(), r.getOwnerId()))
                .toList();

        return pageMapper.fromPage(content, modelPage);
    }
}
