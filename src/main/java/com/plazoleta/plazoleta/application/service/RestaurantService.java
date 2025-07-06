package com.plazoleta.plazoleta.application.service;

import com.plazoleta.plazoleta.application.dto.request.SaveRestaurantRequest;
import com.plazoleta.plazoleta.application.dto.response.SaveRestaurantResponse;

public interface RestaurantService {
    SaveRestaurantResponse save(SaveRestaurantRequest request);

}
