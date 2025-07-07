package com.plazoleta.plazoleta.domain;


import com.plazoleta.plazoleta.domain.models.DishModel;
import com.plazoleta.plazoleta.domain.ports.out.DishPersistencePort;
import com.plazoleta.plazoleta.domain.usecases.DishUseCase;
import com.plazoleta.plazoleta.domain.exceptions.UnauthorizedAccessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
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
        DishModel dishModel = new DishModel();
        dishModel.setName("Test");
        dishModel.setPrice(100);
        dishModel.setDescription("Desc");
        dishModel.setUrlImage("url");
        dishModel.setCategory("Cat");
        dishModel.setRestaurantId(1L);
        dishModel.setActive(true);

        useCase.save(dishModel);

        verify(persistencePort, times(1)).save(dishModel);
        verifyNoMoreInteractions(persistencePort);
    }

    @Test
    void update_ShouldDelegateToPersistencePort() {
        Long id = 1L;
        DishModel updatedModel = new DishModel();
        updatedModel.setRestaurantId(1L);
        updatedModel.setActive(false);

        useCase.update(id, updatedModel);

        verify(persistencePort, times(1)).update(id, updatedModel);
        verifyNoMoreInteractions(persistencePort);
    }

    @Test
    void updateStatus_WhenOwnerMatches_ShouldUpdateAndDelegate() {
        Long dishId = 42L;

        DishModel existing = new DishModel();
        existing.setRestaurantId(dishId);
        existing.setActive(true);
        when(persistencePort.getById(dishId)).thenReturn(existing);

        Claims claims = mock(Claims.class);
        when(claims.get("ownerId", Long.class)).thenReturn(dishId);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                claims, null, Collections.emptyList()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        DishModel toUpdate = new DishModel();
        toUpdate.setActive(false);
        useCase.updateStatus(dishId, toUpdate);

        assertFalse(existing.isActive());
        verify(persistencePort).updateStatus(dishId, existing);
    }

    @Test
    void updateStatus_WhenOwnerMismatch_ShouldThrowUnauthorized() {
        Long dishId = 99L;

        DishModel existing = new DishModel();
        existing.setRestaurantId(100L);
        when(persistencePort.getById(dishId)).thenReturn(existing);

        Claims claims = Jwts.claims();
        claims.put("ownerId", 101L);
        Authentication auth = new UsernamePasswordAuthenticationToken(
                claims, null, Collections.emptyList()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        DishModel toUpdate = new DishModel();
        toUpdate.setActive(false);

        UnauthorizedAccessException ex = assertThrows(
                UnauthorizedAccessException.class,
                () -> useCase.updateStatus(dishId, toUpdate)
        );
        assertTrue(ex.getMessage().contains("not allowed"));
        verify(persistencePort, never()).updateStatus(anyLong(), any());
    }

}

