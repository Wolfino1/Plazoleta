package com.plazoleta.plazoleta.domain;

import com.plazoleta.plazoleta.domain.models.RestaurantModel;
import com.plazoleta.plazoleta.domain.ports.out.RestaurantPersistencePort;
import com.plazoleta.plazoleta.domain.usecases.RestaurantUseCase;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock
    private RestaurantPersistencePort restaurantPersistencePort;

    @InjectMocks
    private RestaurantUseCase useCase;

    private RestaurantModel sampleModel;
    private final Long SAMPLE_ID = 1L;

    @BeforeEach
    void setup() {
        sampleModel = new RestaurantModel(
                SAMPLE_ID,
                "Sabor Santandereano",
                "900987654",
                "Carrera 27 #36-10, Bucaramanga",
                "+573227654321",
                "https://cdn.ejemplo.com/logos/sabor-santandereano.png",
                1L
        );
    }

    @Test
    void save_callsPersistenceSave() {
        useCase.save(sampleModel);
        verify(restaurantPersistencePort, times(1)).save(sampleModel);
    }

    @Test
    void getRestaurants_delegatesAllParameters() {
        Integer page = 0;
        Integer size = 5;
        Long id = 2L;
        String name = "Foo";
        Long ownerId = 3L;
        String logoUrl = "logo.png";
        String sortBy = "name";
        boolean orderAsc = false;

        PagedResult<RestaurantModel> expected = mock(PagedResult.class);
        when(restaurantPersistencePort.getRestaurants(page, size, id, name, ownerId, logoUrl, sortBy, orderAsc))
                .thenReturn(expected);

        PagedResult<RestaurantModel> actual = useCase.getRestaurants(page, size, id, name, ownerId, logoUrl, sortBy, orderAsc);

        assertSame(expected, actual);
    }

    @Test
    void getRestaurantById_nullId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> useCase.getRestaurantById(null));
    }

    @Test
    void getRestaurantById_notFound_throwsRuntimeException() {
        when(restaurantPersistencePort.getUserById(SAMPLE_ID)).thenReturn(null);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> useCase.getRestaurantById(SAMPLE_ID));
        assertTrue(ex.getMessage().contains(SAMPLE_ID.toString()));
    }

    @Test
    void getRestaurantById_found_returnsModel() {
        when(restaurantPersistencePort.getUserById(SAMPLE_ID)).thenReturn(sampleModel);
        RestaurantModel actual = useCase.getRestaurantById(SAMPLE_ID);
        assertEquals(sampleModel, actual);
    }
}

