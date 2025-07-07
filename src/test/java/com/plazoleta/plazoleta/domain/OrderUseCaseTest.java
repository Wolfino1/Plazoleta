package com.plazoleta.plazoleta.domain;

import com.plazoleta.plazoleta.domain.models.OrderItemModel;
import com.plazoleta.plazoleta.domain.models.OrderModel;
import com.plazoleta.plazoleta.domain.models.OrderStatus;
import com.plazoleta.plazoleta.domain.ports.out.OrderPersistencePort;
import com.plazoleta.plazoleta.domain.usecases.OrderUseCase;
import com.plazoleta.plazoleta.domain.exceptions.BusinessException;
import com.plazoleta.plazoleta.domain.util.constants.DomainConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderUseCaseTest {

    private OrderPersistencePort persistencePort;
    private OrderUseCase useCase;

    @BeforeEach
    void setUp() {
        persistencePort = mock(OrderPersistencePort.class);
        useCase = new OrderUseCase(persistencePort);
    }

    @Test
    void save_whenNoOpenOrders_thenSetsPendingAndSaves() {
        OrderModel order = new OrderModel(null, 4L, 1L, null,
                List.of(new OrderItemModel(1L, 1), new OrderItemModel(2L, 3)));
        order.setClientId(123L);
        when(persistencePort.existsByClientAndStatusIn(
                123L,
                List.of(OrderStatus.PENDIENTE, OrderStatus.EN_PREPARACION)
        )).thenReturn(false);

        useCase.save(order);

        assertEquals(OrderStatus.PENDIENTE, order.getStatus());
        verify(persistencePort).save(order);
    }

    @Test
    void save_whenHasOpenOrders_thenThrowsBusinessException() {
        OrderModel order = new OrderModel(null, 4L, 1L, null,
                List.of(new OrderItemModel(1L, 1), new OrderItemModel(2L, 3)));
        order.setClientId(456L);
        when(persistencePort.existsByClientAndStatusIn(
                456L,
                List.of(OrderStatus.PENDIENTE, OrderStatus.EN_PREPARACION)
        )).thenReturn(true);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> useCase.save(order)
        );
        assertEquals(DomainConstants.ORDER_ALREADY_CREATED, ex.getMessage());
        verify(persistencePort, never()).save(any());
    }
}
