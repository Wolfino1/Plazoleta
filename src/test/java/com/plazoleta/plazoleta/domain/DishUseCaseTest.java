package com.plazoleta.plazoleta.domain;

import com.plazoleta.plazoleta.domain.models.DishModel;
import com.plazoleta.plazoleta.domain.ports.out.DishPersistencePort;
import com.plazoleta.plazoleta.domain.usecases.DishUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class DishUseCaseTest {

    private DishPersistencePort persistencePort;
    private DishUseCase useCase;

    @BeforeEach
    void setUp() {
        persistencePort = mock(DishPersistencePort.class);
        useCase = new DishUseCase(persistencePort);
    }

    @Test
    void save_ShouldDelegateToPersistencePort() {
        DishModel dummy = new DishModel(
                "Test Dish",
                1000,
                "Some description",
                "http://img.url/dish.png",
                "Category",
                1L
        );

        useCase.save(dummy);

        verify(persistencePort, times(1)).save(dummy);
        verifyNoMoreInteractions(persistencePort);
    }
}
