package com.plazoleta.plazoleta.application.service.Impl;

import com.plazoleta.plazoleta.application.dto.request.SaveDishRequest;
import com.plazoleta.plazoleta.application.dto.request.UpdateDishRequest;
import com.plazoleta.plazoleta.application.dto.response.SaveDishResponse;
import com.plazoleta.plazoleta.application.mappers.DishDtoMapper;
import com.plazoleta.plazoleta.application.service.DishService;
import com.plazoleta.plazoleta.common.configurations.util.Constants;
import com.plazoleta.plazoleta.domain.models.DishModel;
import com.plazoleta.plazoleta.domain.ports.in.DishServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
        // mapea sólo precio y descripción a un DishModel
        DishModel updatedFields = dishDtoMapper.updateRequestToModel(request);
        // delega al UseCase con el id y los campos nuevos
        dishServicePort.update(id, updatedFields);
        return new SaveDishResponse(
                Constants.UPDATE_DISH_RESPONSE_MESSAGE,
                LocalDateTime.now()
        );
    }
}
