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

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock
    private RestaurantPersistencePort persistencePort;

    private RestaurantUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new RestaurantUseCase(persistencePort);
    }

    @Test
    void save_shouldDelegateToPersistencePort() {
        RestaurantModel model = new RestaurantModel(
                1L,
                "Prueba Restaurantes",
                "123456789",
                "Calle Falsa 123",
                "+571234567890",
                "https://logo.test/img.png",
                1L
        );

        useCase.save(model);

        verify(persistencePort).save(model);
        verifyNoMoreInteractions(persistencePort);
    }

    @Test
    void save_withNullModel_shouldDelegateNullToPersistencePort() {
        useCase.save(null);

        verify(persistencePort).save(null);
        verifyNoMoreInteractions(persistencePort);
    }
    @Test
    void whenGetRestaurants_thenDelegatesToPersistencePort() {
        int page = 0;
        int size = 5;
        String name = "Testaurant";
        String logoUrl = "https://logo.test/img.png";
        String sortBy = "name";
        boolean orderAsc = true;

        RestaurantModel r1 = new RestaurantModel(
                1L,
                "Rest A",
                "11111111",
                "Addr A",
                "+5700000001",
                "https://logo.test/a.png",
                10L
        );
        RestaurantModel r2 = new RestaurantModel(
                2L,
                "Rest B",
                "22222222",
                "Addr B",
                "+5700000002",
                "https://logo.test/b.png",
                20L
        );

        PagedResult<RestaurantModel> expected = new PagedResult<>(
                List.of(r1, r2),
                page,
                size,
                2L
        );
        when(persistencePort.getRestaurants(page, size, name, logoUrl, sortBy, orderAsc))
                .thenReturn(expected);

        PagedResult<RestaurantModel> actual = useCase.getRestaurants(
                page, size, name, logoUrl, sortBy, orderAsc
        );

        verify(persistencePort).getRestaurants(page, size, name, logoUrl, sortBy, orderAsc);
        assertSame(expected, actual);
    }

}
