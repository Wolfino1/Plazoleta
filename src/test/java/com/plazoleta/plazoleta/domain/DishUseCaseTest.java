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

    @Test
    void update_ShouldDelegateToPersistencePort() {
        Long id = 1L;
        DishModel updatedModel = new DishModel(
                "Mute Santandereano",
                30000,
                "Actualización de descripción",
                "https://img.url",
                "Sopa",
                1L
        );

        useCase.update(id, updatedModel);

        verify(persistencePort, times(1)).update(id, updatedModel);
        verifyNoMoreInteractions(persistencePort);
    }
}
