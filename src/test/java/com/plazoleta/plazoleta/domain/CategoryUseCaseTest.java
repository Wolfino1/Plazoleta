package com.plazoleta.plazoleta.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.plazoleta.plazoleta.domain.models.CategoryModel;
import com.plazoleta.plazoleta.domain.ports.out.CategoryPersistencePort;
import com.plazoleta.plazoleta.domain.usecases.CategoryUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryUseCaseTest {

    @Mock
    private CategoryPersistencePort categoryPersistencePort;

    @InjectMocks
    private CategoryUseCase useCase;

    private final Long EXISTING_ID = 1L;
    private final Long MISSING_ID = 2L;
    private CategoryModel sampleCategory;

    @BeforeEach
    void setUp() {
        sampleCategory = new CategoryModel(EXISTING_ID, "Comida", "Categoría de alimentos típicos");
    }

    @Test
    void whenCategoryExists_thenReturnOptionalWithModel() {
        // arrange
        when(categoryPersistencePort.getById(EXISTING_ID))
                .thenReturn(Optional.of(sampleCategory));

        // act
        Optional<CategoryModel> result = useCase.getById(EXISTING_ID);

        // assert
        assertTrue(result.isPresent(), "Debe devolver Optional con valor");
        assertEquals(sampleCategory, result.get(), "El modelo devuelto debe ser el mismo");

        // verify that the port was called correctamente
        verify(categoryPersistencePort, times(1)).getById(EXISTING_ID);
    }

    @Test
    void whenCategoryDoesNotExist_thenReturnEmptyOptional() {
        // arrange
        when(categoryPersistencePort.getById(MISSING_ID))
                .thenReturn(Optional.empty());

        // act
        Optional<CategoryModel> result = useCase.getById(MISSING_ID);

        // assert
        assertFalse(result.isPresent(), "Debe devolver Optional vacío");

        // verify
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(categoryPersistencePort).getById(captor.capture());
        assertEquals(MISSING_ID, captor.getValue(), "Debe invocar getById con el ID pedido");
    }
}

