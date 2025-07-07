package com.plazoleta.plazoleta.domain;

import com.plazoleta.plazoleta.domain.models.DishModel;
import com.plazoleta.plazoleta.domain.ports.out.DishPersistencePort;
import com.plazoleta.plazoleta.domain.exceptions.UnauthorizedAccessException;
import com.plazoleta.plazoleta.domain.usecases.DishUseCase;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class DishUseCaseTest {

    private DishPersistencePort persistencePort;
    private DishUseCase useCase;

    @BeforeEach
    void setUp() {
        persistencePort = mock(DishPersistencePort.class);
        useCase = new DishUseCase(persistencePort);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void save_ShouldDelegateToPersistencePort() {
        DishModel dishModel = new DishModel("Test", 100, "Desc", "url", "Cat", 1L);
        dishModel.setActive(true);
        useCase.save(dishModel);
        verify(persistencePort).save(dishModel);
        verifyNoMoreInteractions(persistencePort);
    }

    @Test
    void update_ShouldDelegateToPersistencePort() {
        Long id = 1L;
        DishModel updatedModel = new DishModel("Test", 100, "Desc", "url", "Cat", 1L);
        updatedModel.setActive(false);
        useCase.update(id, updatedModel);
        verify(persistencePort).update(id, updatedModel);
        verifyNoMoreInteractions(persistencePort);
    }

    @Test
    void updateStatus_WhenOwnerMatches_ShouldUpdateAndDelegate() {
        Long dishId = 42L;
        DishModel existing = new DishModel("Foo", 123, "X", "u", "c", dishId);
        existing.setActive(true);
        when(persistencePort.getById(dishId)).thenReturn(existing);

        Claims claims = mock(Claims.class);
        when(claims.get("ownerId", Long.class)).thenReturn(dishId);
        Authentication auth = new UsernamePasswordAuthenticationToken(claims, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);

        DishModel toUpdate = new DishModel("Foo", 123, "X", "u", "c", dishId);
        toUpdate.setActive(false);
        useCase.updateStatus(dishId, toUpdate);
        assertThat(existing.isActive()).isFalse();
        verify(persistencePort).updateStatus(dishId, existing);
    }

    @Test
    void updateStatus_WhenOwnerMismatch_ShouldThrowUnauthorized() {
        Long dishId = 99L;
        DishModel existing = new DishModel("A", 1, "D", "u", "c", 100L);
        when(persistencePort.getById(dishId)).thenReturn(existing);

        Claims claims = Jwts.claims();
        claims.put("ownerId", 101L);
        Authentication auth = new UsernamePasswordAuthenticationToken(claims, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);

        DishModel toUpdate = new DishModel("A", 1, "D", "u", "c", 100L);
        toUpdate.setActive(false);
        assertThrows(UnauthorizedAccessException.class, () -> useCase.updateStatus(dishId, toUpdate));
        verify(persistencePort, never()).updateStatus(anyLong(), any());
    }

    @Test
    void getDishes_ShouldDelegateToPersistencePort() {
        Long restaurantId = 1L;
        int page = 0;
        int size = 5;
        String name = "pollo";
        Integer price = null;
        String description = null;
        String urlImage = null;
        String category = null;
        boolean active = true;
        String sortBy = "price";
        boolean orderAsc = false;

        DishModel m1 = new DishModel(
                "Pollo Asado",
                20000,
                "Tradicional pollo a la brasa",
                "https://ejemplo.com/pollo-asado.png",
                "Carnes",
                restaurantId
        );
        DishModel m2 = new DishModel(
                "Pollo Frito",
                18000,
                "Crujiente y jugoso",
                "https://ejemplo.com/pollo-frito.png",
                "Carnes",
                restaurantId
        );
        PagedResult<DishModel> expected = new PagedResult<>(List.of(m1, m2), page, size, 2L);
        when(persistencePort.getDishes(
                restaurantId, page, size,
                name, price, description, urlImage, category,
                active, sortBy, orderAsc
        )).thenReturn(expected);

        PagedResult<DishModel> actual = useCase.getDishes(
                restaurantId, page, size,
                name, price, description, urlImage, category,
                active, sortBy, orderAsc
        );
        verify(persistencePort).getDishes(
                restaurantId, page, size,
                name, price, description, urlImage, category,
                active, sortBy, orderAsc
        );
        assertThat(actual).isSameAs(expected);
    }

    @Test
    void getDishes_WhenPersistencePortThrows_ShouldPropagate() {
        when(persistencePort.getDishes(
                anyLong(), anyInt(), anyInt(),
                any(), any(), any(), any(), any(),
                anyBoolean(), any(), anyBoolean()
        )).thenThrow(new RuntimeException("DB caído"));

        assertThrows(
                RuntimeException.class,
                () -> useCase.getDishes(1L, 0, 10, null, null, null, null, null, true, "name", true)
        );
    }
}

