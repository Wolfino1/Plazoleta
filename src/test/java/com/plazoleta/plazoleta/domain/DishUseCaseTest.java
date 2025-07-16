package com.plazoleta.plazoleta.domain;

import com.plazoleta.plazoleta.domain.exceptions.WrongArgumentException;
import com.plazoleta.plazoleta.domain.models.DishModel;
import com.plazoleta.plazoleta.domain.models.RestaurantModel;
import com.plazoleta.plazoleta.domain.ports.out.DishPersistencePort;
import com.plazoleta.plazoleta.domain.exceptions.UnauthorizedAccessException;
import com.plazoleta.plazoleta.domain.ports.out.RestaurantPersistencePort;
import com.plazoleta.plazoleta.domain.usecases.DishUseCase;
import com.plazoleta.plazoleta.domain.util.constants.DomainConstants;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;

@ExtendWith(MockitoExtension.class)
class DishUseCaseTest {

    @Mock
    private DishPersistencePort dishPersistencePort;

    @Mock
    private RestaurantPersistencePort restaurantPersistencePort;

    @InjectMocks
    private DishUseCase useCase;

    private DishModel sampleDish;
    private RestaurantModel sampleRestaurant;
    private Claims claims;
    private Authentication auth;
    private final Long DISH_ID = 1L;
    private final Long RESTAURANT_ID = 10L;
    private final Long OWNER_ID = 100L;

    @BeforeEach
    void setUp() {
        sampleDish = new DishModel(DISH_ID, "Arepa", 5000, "Deliciosa arepa santandereana", "url", 2L, RESTAURANT_ID);
        sampleRestaurant = new RestaurantModel(RESTAURANT_ID, "Sabor", "111", "dir", "+573000000000", "logo", OWNER_ID);
        claims = mock(Claims.class);
        when(claims.get(DomainConstants.OWNER_ID, Long.class)).thenReturn(OWNER_ID);
        auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(claims);
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);
    }

    @Test
    void save_success() {
        when(restaurantPersistencePort.findById(RESTAURANT_ID)).thenReturn(Optional.of(sampleRestaurant));
        useCase.save(sampleDish);
        verify(dishPersistencePort).save(sampleDish);
    }

    @Test
    void save_nonExistingRestaurant_throws() {
        when(restaurantPersistencePort.findById(RESTAURANT_ID)).thenReturn(Optional.empty());
        assertThrows(WrongArgumentException.class, () -> useCase.save(sampleDish));
    }

    @Test
    void save_unauthorized_throws() {
        RestaurantModel other = new RestaurantModel(RESTAURANT_ID, "X", "1", "a", "+573000000000", "logo", OWNER_ID + 1);
        when(restaurantPersistencePort.findById(RESTAURANT_ID)).thenReturn(Optional.of(other));
        assertThrows(UnauthorizedAccessException.class, () -> useCase.save(sampleDish));
    }

    @Test
    void update_success() {
        DishModel updated = new DishModel(DISH_ID, "X", 6000, "desc2", "url", 2L, RESTAURANT_ID);
        when(dishPersistencePort.getById(DISH_ID)).thenReturn(sampleDish);
        when(restaurantPersistencePort.findById(RESTAURANT_ID)).thenReturn(Optional.of(sampleRestaurant));
        useCase.update(DISH_ID, updated);
        assertEquals(6000, sampleDish.getPrice());
        assertEquals("desc2", sampleDish.getDescription());
        verify(dishPersistencePort).update(DISH_ID, sampleDish);
    }

    @Test
    void update_notFound_throws() {
        when(dishPersistencePort.getById(DISH_ID)).thenReturn(null);
        assertThrows(WrongArgumentException.class, () -> useCase.update(DISH_ID, sampleDish));
    }

    @Test
    void update_nonExistingRestaurant_throws() {
        when(dishPersistencePort.getById(DISH_ID)).thenReturn(sampleDish);
        when(restaurantPersistencePort.findById(RESTAURANT_ID)).thenReturn(Optional.empty());
        assertThrows(WrongArgumentException.class, () -> useCase.update(DISH_ID, sampleDish));
    }

    @Test
    void update_unauthorized_throws() {
        when(dishPersistencePort.getById(DISH_ID)).thenReturn(sampleDish);
        RestaurantModel other = new RestaurantModel(RESTAURANT_ID, "X", "1", "a", "+573000000000", "logo", OWNER_ID + 1);
        when(restaurantPersistencePort.findById(RESTAURANT_ID)).thenReturn(Optional.of(other));
        assertThrows(UnauthorizedAccessException.class, () -> useCase.update(DISH_ID, sampleDish));
    }

    @Test
    void updateStatus_success() {
        DishModel newStatus = new DishModel();
        newStatus.setActive(false);
        when(dishPersistencePort.getById(DISH_ID)).thenReturn(sampleDish);
        when(restaurantPersistencePort.findById(RESTAURANT_ID)).thenReturn(Optional.of(sampleRestaurant));
        useCase.updateStatus(DISH_ID, newStatus);
        assertFalse(sampleDish.isActive());
        verify(dishPersistencePort).updateStatus(DISH_ID, sampleDish);
    }

    @Test
    void updateStatus_notFound_throws() {
        when(dishPersistencePort.getById(DISH_ID)).thenReturn(null);
        assertThrows(WrongArgumentException.class, () -> useCase.updateStatus(DISH_ID, sampleDish));
    }

    @Test
    void updateStatus_nonExistingRestaurant_throws() {
        when(dishPersistencePort.getById(DISH_ID)).thenReturn(sampleDish);
        when(restaurantPersistencePort.findById(RESTAURANT_ID)).thenReturn(Optional.empty());
        assertThrows(WrongArgumentException.class, () -> useCase.updateStatus(DISH_ID, sampleDish));
    }

    @Test
    void updateStatus_unauthorized_throws() {
        when(dishPersistencePort.getById(DISH_ID)).thenReturn(sampleDish);
        RestaurantModel other = new RestaurantModel(RESTAURANT_ID, "X", "1", "a", "+573000000000", "logo", OWNER_ID + 1);
        when(restaurantPersistencePort.findById(RESTAURANT_ID)).thenReturn(Optional.of(other));
        assertThrows(UnauthorizedAccessException.class, () -> useCase.updateStatus(DISH_ID, sampleDish));
    }
}