package com.plazoleta.plazoleta.application.service.Impl;

import com.plazoleta.plazoleta.application.dto.request.SaveDishRequest;
import com.plazoleta.plazoleta.application.dto.request.UpdateDishRequest;
import com.plazoleta.plazoleta.application.dto.request.UpdateDishStatusRequest;
import com.plazoleta.plazoleta.application.dto.response.DishResponse;
import com.plazoleta.plazoleta.application.dto.response.SaveDishResponse;
import com.plazoleta.plazoleta.application.mappers.DishDtoMapper;
import com.plazoleta.plazoleta.application.service.DishService;
import com.plazoleta.plazoleta.common.configurations.util.Constants;
import com.plazoleta.plazoleta.domain.models.DishModel;
import com.plazoleta.plazoleta.domain.ports.in.DishServicePort;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {
    private final DishServicePort dishServicePort;
    private final DishDtoMapper dishDtoMapper;

    @Override
    public SaveDishResponse save(SaveDishRequest request) {
        dishServicePort.save(dishDtoMapper.requestToModel(request));
        return new SaveDishResponse(Constants.SAVE_DISH_RESPONSE_MESSAGE, LocalDateTime.now());
    }

    @Override
    public SaveDishResponse update(Long id, UpdateDishRequest request) {
        DishModel updatedFields = dishDtoMapper.updateRequestToModel(request);
        dishServicePort.update(id, updatedFields);
        return new SaveDishResponse(
                Constants.UPDATE_DISH_RESPONSE_MESSAGE,
                LocalDateTime.now()
        );
    }

    @Override
    public SaveDishResponse updateDishStatus(Long id, UpdateDishStatusRequest statusRequest) {
        DishModel updatedFields = dishDtoMapper.updateStatusRequestToModel(statusRequest);
        dishServicePort.updateStatus(id, updatedFields);
        return new SaveDishResponse(
                Constants.UPDATE_DISH_RESPONSE_MESSAGE,
                LocalDateTime.now()
        );
    }

    @Override
    public PagedResult<DishResponse> getDishes(
            Long restaurantId,
            Integer page,
            Integer size,
            String name,
            Integer price,
            String description,
            String urlImage,
            String category,
            boolean active,
            String sortBy,
            boolean orderAsc) {

        PagedResult<DishModel> modelPage = dishServicePort.getDishes(
                restaurantId,
                page,
                size,
                name,
                price,
                description,
                urlImage,
                category,
                active,
                sortBy,
                orderAsc
        );

        List<DishResponse> content = modelPage.getContent().stream()
                .map(d -> new DishResponse(
                        d.getName(),
                        d.getPrice(),
                        d.getDescription(),
                        d.getUrlImage(),
                        d.getCategory(),
                        d.isActive()
                ))
                .toList();

        return new PagedResult<>(
                content,
                modelPage.getPage(),
                modelPage.getSize(),
                modelPage.getTotalElements()
        );
    }

}
