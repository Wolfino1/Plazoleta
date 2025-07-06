package com.plazoleta.plazoleta.domain;

import com.plazoleta.plazoleta.domain.models.RestaurantModel;
import com.plazoleta.plazoleta.domain.ports.out.RestaurantPersistencePort;
import com.plazoleta.plazoleta.domain.usecases.RestaurantUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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
}
