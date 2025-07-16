package com.plazoleta.plazoleta.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.IllegalStateException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.plazoleta.plazoleta.domain.usecases.OrderUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.plazoleta.plazoleta.domain.exceptions.*;
import com.plazoleta.plazoleta.domain.models.*;
import com.plazoleta.plazoleta.domain.util.constants.DomainConstants;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;
import com.plazoleta.plazoleta.domain.ports.out.*;
import com.plazoleta.plazoleta.infrastructure.client.dto.CreateTraceabilityRequest;
import com.plazoleta.plazoleta.infrastructure.security.JwtUtil;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {

    @Mock
    private OrderPersistencePort orderPersistencePort;
    @Mock
    private DishPersistencePort dishPersistencePort;
    @Mock
    private RestaurantPersistencePort restaurantPersistencePort;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private NotificationClientPort notificationClient;
    @Mock
    private TraceabilityClientPort traceabilityClientPort;

    @InjectMocks
    private OrderUseCase useCase;

    private OrderModel newOrder;
    private OrderItemModel item;
    private final String AUTH = "Bearer token";
    private final Long CLIENT_ID = 10L;
    private final Long RESTAURANT_ID = 20L;
    private final Long EMPLOYEE_ID = 30L;
    private final Integer PIN = 1234;
    private final String PHONE = "+571234567890";

    @BeforeEach
    void setUp() {
        item = new OrderItemModel(1L, 2);
        newOrder = new OrderModel(null, CLIENT_ID, RESTAURANT_ID, null, List.of(item), null, null, PHONE);
    }

    @Test
    void save_whenOpenOrderExists_throwsBusinessException() {
        when(orderPersistencePort.existsByClientAndStatusIn(anyLong(), anyList()))
                .thenReturn(true);
        assertThrows(BusinessException.class, () -> useCase.save(newOrder));
    }

    @Test
    void save_whenRestaurantNotFound_throwsWrongArgumentException() {
        when(orderPersistencePort.existsByClientAndStatusIn(anyLong(), anyList()))
                .thenReturn(false);
        when(restaurantPersistencePort.findById(RESTAURANT_ID))
                .thenReturn(Optional.empty());
        assertThrows(WrongArgumentException.class, () -> useCase.save(newOrder));
    }

    @Test
    void save_whenDishNotFound_throwsWrongArgumentException() {
        when(orderPersistencePort.existsByClientAndStatusIn(anyLong(), anyList()))
                .thenReturn(false);
        when(restaurantPersistencePort.findById(RESTAURANT_ID))
                .thenReturn(Optional.of(mock(RestaurantModel.class)));
        when(dishPersistencePort.findById(item.getDishId()))
                .thenReturn(Optional.empty());
        assertThrows(WrongArgumentException.class, () -> useCase.save(newOrder));
    }

    @Test
    void save_whenDishFromOtherRestaurant_throwsWrongArgumentException() {
        when(orderPersistencePort.existsByClientAndStatusIn(anyLong(), anyList()))
                .thenReturn(false);
        when(restaurantPersistencePort.findById(RESTAURANT_ID))
                .thenReturn(Optional.of(mock(RestaurantModel.class)));
        DishModel d = new DishModel(item.getDishId(), "X", 1, "d", "u", 1L, RESTAURANT_ID + 1);
        when(dishPersistencePort.findById(item.getDishId()))
                .thenReturn(Optional.of(d));
        assertThrows(WrongArgumentException.class, () -> useCase.save(newOrder));
    }

    @Test
    void save_whenPhoneInvalid_throwsWrongArgumentException() {
        when(orderPersistencePort.existsByClientAndStatusIn(anyLong(), anyList()))
                .thenReturn(false);
        when(restaurantPersistencePort.findById(RESTAURANT_ID))
                .thenReturn(Optional.of(mock(RestaurantModel.class)));
        when(dishPersistencePort.findById(item.getDishId()))
                .thenReturn(Optional.of(new DishModel(item.getDishId(), "X", 1, "d", "u", 1L, RESTAURANT_ID)));
        newOrder.setPhoneNumber("123");
        assertThrows(WrongArgumentException.class, () -> useCase.save(newOrder));
    }

    @Test
    void save_validOrder_savesWithPendingStatus() {
        when(orderPersistencePort.existsByClientAndStatusIn(anyLong(), anyList()))
                .thenReturn(false);
        when(restaurantPersistencePort.findById(RESTAURANT_ID))
                .thenReturn(Optional.of(mock(RestaurantModel.class)));
        when(dishPersistencePort.findById(item.getDishId()))
                .thenReturn(Optional.of(new DishModel(item.getDishId(), "X", 1, "d", "u", 1L, RESTAURANT_ID)));
        useCase.save(newOrder);
        assertEquals(OrderStatus.PENDIENTE, newOrder.getStatus());
        verify(orderPersistencePort).save(newOrder);
    }

    @Test
    void getOrdersByFilter_whenNotAllowed_throwsUnauthorizedException() {
        when(jwtUtil.getRestaurantIdFromSecurityContext()).thenReturn(99L);
        assertThrows(UnauthorizedException.class,
                () -> useCase.getOrdersByFilter(RESTAURANT_ID, 0, 1, CLIENT_ID, OrderStatus.PENDIENTE));
    }

    @Test
    void getOrdersByFilter_allowed_delegatesToPort() {
        when(jwtUtil.getRestaurantIdFromSecurityContext()).thenReturn(RESTAURANT_ID);
        PagedResult<OrderModel> page = mock(PagedResult.class);
        when(orderPersistencePort.getOrdersByFilter(RESTAURANT_ID, 0, 1, CLIENT_ID, OrderStatus.PENDIENTE))
                .thenReturn(page);
        assertSame(page, useCase.getOrdersByFilter(RESTAURANT_ID, 0, 1, CLIENT_ID, OrderStatus.PENDIENTE));
    }

    @Test
    void assignEmployee_nullEmployeeId_throwsWrongArgumentException() {
        OrderModel u = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.PENDIENTE, List.of(item), null, PIN, PHONE);
        assertThrows(WrongArgumentException.class, () -> useCase.assignEmployee(1L, u, AUTH));
    }

    @Test
    void assignEmployee_unauthorizedEmployee_throwsUnauthorizedException() {
        OrderModel u = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.PENDIENTE, List.of(item), EMPLOYEE_ID, PIN, PHONE);
        when(jwtUtil.getEmployeeIdFromSecurityContext()).thenReturn(EMPLOYEE_ID + 1);
        assertThrows(UnauthorizedException.class, () -> useCase.assignEmployee(1L, u, AUTH));
    }

    @Test
    void assignEmployee_notFound_throwsWrongArgumentException() {
        OrderModel u = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.PENDIENTE, List.of(item), EMPLOYEE_ID, PIN, PHONE);
        when(jwtUtil.getEmployeeIdFromSecurityContext()).thenReturn(EMPLOYEE_ID);
        when(orderPersistencePort.getById(1L)).thenReturn(null);
        assertThrows(WrongArgumentException.class, () -> useCase.assignEmployee(1L, u, AUTH));
    }

    @Test
    void assignEmployee_notPending_throwsIllegalStateException() {
        OrderModel existing = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.EN_PREPARACION, List.of(item), null, PIN, PHONE);
        OrderModel u = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.PENDIENTE, List.of(item), EMPLOYEE_ID, PIN, PHONE);
        when(jwtUtil.getEmployeeIdFromSecurityContext()).thenReturn(EMPLOYEE_ID);
        when(orderPersistencePort.getById(1L)).thenReturn(existing);
        when(restaurantPersistencePort.findById(RESTAURANT_ID)).thenReturn(Optional.of(mock(RestaurantModel.class)));
        when(jwtUtil.getRestaurantIdFromSecurityContext()).thenReturn(RESTAURANT_ID);
        assertThrows(IllegalStateException.class, () -> useCase.assignEmployee(1L, u, AUTH));
    }

    @Test
    void assignEmployee_success_assignsAndTraces() {
        OrderModel existing = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.PENDIENTE, List.of(item), null, PIN, PHONE);
        OrderModel u = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.PENDIENTE, List.of(item), EMPLOYEE_ID, PIN, PHONE);
        when(jwtUtil.getEmployeeIdFromSecurityContext()).thenReturn(EMPLOYEE_ID);
        when(orderPersistencePort.getById(1L)).thenReturn(existing);
        when(restaurantPersistencePort.findById(RESTAURANT_ID)).thenReturn(Optional.of(mock(RestaurantModel.class)));
        when(jwtUtil.getRestaurantIdFromSecurityContext()).thenReturn(RESTAURANT_ID);
        OrderModel result = useCase.assignEmployee(1L, u, AUTH);
        assertEquals(EMPLOYEE_ID, result.getEmployeeId());
        assertEquals(OrderStatus.EN_PREPARACION, result.getStatus());
        verify(orderPersistencePort).assignOrder(eq(1L), any());
        verify(traceabilityClientPort).createTrace(any(CreateTraceabilityRequest.class), eq(AUTH));
    }

    @Test
    void changeStatus_nullStatus_throwsWrongArgumentException() {
        OrderModel u = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, null, List.of(item), EMPLOYEE_ID, PIN, PHONE);
        assertThrows(WrongArgumentException.class, () -> useCase.changeStatus(1L, u, AUTH));
    }

    @Test
    void changeStatus_invalidStatus_throwsWrongArgumentException() {
        OrderModel u = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.PENDIENTE, List.of(item), EMPLOYEE_ID, PIN, PHONE);
        assertThrows(WrongArgumentException.class, () -> useCase.changeStatus(1L, u, AUTH));
    }

    @Test
    void changeStatus_notFound_throwsWrongArgumentException() {
        OrderModel u = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.LISTO, List.of(item), EMPLOYEE_ID, PIN, PHONE);
        when(orderPersistencePort.getById(1L)).thenReturn(null);
        assertThrows(WrongArgumentException.class, () -> useCase.changeStatus(1L, u, AUTH));
    }

    @Test
    void changeStatus_unauthorized_throwsUnauthorizedException() {
        OrderModel existing = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.EN_PREPARACION, List.of(item), EMPLOYEE_ID, PIN, PHONE);
        OrderModel u = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.LISTO, List.of(item), EMPLOYEE_ID, PIN, PHONE);
        when(orderPersistencePort.getById(1L)).thenReturn(existing);
        when(jwtUtil.getEmployeeIdFromSecurityContext()).thenReturn(EMPLOYEE_ID + 1);
        assertThrows(UnauthorizedException.class, () -> useCase.changeStatus(1L, u, AUTH));
    }

    @Test
    void changeStatus_idempotent_returnsSameWhenAlreadyListo() {
        OrderModel existing = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.LISTO, List.of(item), EMPLOYEE_ID, PIN, PHONE);
        OrderModel u = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.LISTO, List.of(item), EMPLOYEE_ID, PIN, PHONE);
        when(orderPersistencePort.getById(1L)).thenReturn(existing);
        when(jwtUtil.getEmployeeIdFromSecurityContext()).thenReturn(EMPLOYEE_ID);
        assertSame(existing, useCase.changeStatus(1L, u, AUTH));
    }

    @Test
    void changeStatus_wrongState_throwsIllegalStateException() {
        OrderModel existing = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.PENDIENTE, List.of(item), EMPLOYEE_ID, PIN, PHONE);
        OrderModel u = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.LISTO, List.of(item), EMPLOYEE_ID, PIN, PHONE);
        when(orderPersistencePort.getById(1L)).thenReturn(existing);
        when(jwtUtil.getEmployeeIdFromSecurityContext()).thenReturn(EMPLOYEE_ID);
        assertThrows(IllegalStateException.class, () -> useCase.changeStatus(1L, u, AUTH));
    }

    @Test
    void changeStatus_success_changesNotifiesAndTraces() {
        OrderModel existing = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.EN_PREPARACION, List.of(item), EMPLOYEE_ID, PIN, PHONE);
        OrderModel u = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.LISTO, List.of(item), EMPLOYEE_ID, PIN, PHONE);
        when(orderPersistencePort.getById(1L)).thenReturn(existing);
        when(jwtUtil.getEmployeeIdFromSecurityContext()).thenReturn(EMPLOYEE_ID);
        OrderModel result = useCase.changeStatus(1L, u, AUTH);
        assertEquals(OrderStatus.LISTO, result.getStatus());
        verify(orderPersistencePort).changeOrderStatus(eq(1L), any());
        verify(notificationClient).notifyOrderReady(anyLong(), anyLong(), anyString(), anyInt(), anyString());
        verify(traceabilityClientPort).createTrace(any(CreateTraceabilityRequest.class), eq(AUTH));
    }

    @Test
    void completeOrder_nullOrder_throwsWrongArgumentException() {
        when(orderPersistencePort.getById(1L)).thenReturn(null);
        assertThrows(WrongArgumentException.class, () -> useCase.completeOrder(1L, PIN, AUTH));
    }

    @Test
    void completeOrder_nullPin_throwsWrongArgumentException() {
        OrderModel existing = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.LISTO, List.of(item), EMPLOYEE_ID, PIN, PHONE);
        when(orderPersistencePort.getById(1L)).thenReturn(existing);
        assertThrows(WrongArgumentException.class, () -> useCase.completeOrder(1L, null, AUTH));
    }

    @Test
    void completeOrder_unauthorized_throwsUnauthorizedException() {
        OrderModel existing = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.LISTO, List.of(item), EMPLOYEE_ID, PIN, PHONE);
        when(orderPersistencePort.getById(1L)).thenReturn(existing);
        when(jwtUtil.getEmployeeIdFromSecurityContext()).thenReturn(EMPLOYEE_ID + 1);
        assertThrows(UnauthorizedException.class, () -> useCase.completeOrder(1L, PIN, AUTH));
    }

    @Test
    void completeOrder_alreadyDelivered_throwsIllegalStateException() {
        OrderModel existing = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.ENTREGADO, List.of(item), EMPLOYEE_ID, PIN, PHONE);
        when(orderPersistencePort.getById(1L)).thenReturn(existing);
        when(jwtUtil.getEmployeeIdFromSecurityContext()).thenReturn(EMPLOYEE_ID);
        assertThrows(IllegalStateException.class, () -> useCase.completeOrder(1L, PIN, AUTH));
    }

    @Test
    void completeOrder_notReady_throwsIllegalStateException() {
        OrderModel existing = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.EN_PREPARACION, List.of(item), EMPLOYEE_ID, PIN, PHONE);
        when(orderPersistencePort.getById(1L)).thenReturn(existing);
        when(jwtUtil.getEmployeeIdFromSecurityContext()).thenReturn(EMPLOYEE_ID);
        assertThrows(IllegalStateException.class, () -> useCase.completeOrder(1L, PIN, AUTH));
    }

    @Test
    void completeOrder_wrongPin_throwsWrongArgumentException() {
        OrderModel existing = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.LISTO, List.of(item), EMPLOYEE_ID, PIN, PHONE);
        when(orderPersistencePort.getById(1L)).thenReturn(existing);
        when(jwtUtil.getEmployeeIdFromSecurityContext()).thenReturn(EMPLOYEE_ID);
        assertThrows(WrongArgumentException.class, () -> useCase.completeOrder(1L, PIN + 1, AUTH));
    }

    @Test
    void completeOrder_success_changesAndTraces() {
        OrderModel existing = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.LISTO, List.of(item), EMPLOYEE_ID, PIN, PHONE);
        when(orderPersistencePort.getById(1L)).thenReturn(existing);
        when(jwtUtil.getEmployeeIdFromSecurityContext()).thenReturn(EMPLOYEE_ID);
        OrderModel result = useCase.completeOrder(1L, PIN, AUTH);
        assertEquals(OrderStatus.ENTREGADO, result.getStatus());
        verify(orderPersistencePort).changeOrderStatus(eq(1L), any());
        verify(traceabilityClientPort).createTrace(any(CreateTraceabilityRequest.class), eq(AUTH));
    }

    @Test
    void cancelOrder_notPending_throwsWrongArgumentException() {
        OrderModel existing = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.EN_PREPARACION, List.of(item), EMPLOYEE_ID, PIN, PHONE);
        when(orderPersistencePort.getById(1L)).thenReturn(existing);
        assertThrows(WrongArgumentException.class, () -> useCase.cancelOrder(1L, existing, AUTH));
    }

    @Test
    void cancelOrder_unauthorized_throwsUnauthorizedException() {
        OrderModel existing = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.PENDIENTE, List.of(item), EMPLOYEE_ID, PIN, PHONE);
        when(orderPersistencePort.getById(1L)).thenReturn(existing);
        when(jwtUtil.getClientIdFromSecurityContext()).thenReturn(CLIENT_ID + 1);
        assertThrows(UnauthorizedException.class, () -> useCase.cancelOrder(1L, existing, AUTH));
    }

    @Test
    void cancelOrder_success_changesAndTraces() {
        OrderModel existing = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.PENDIENTE, List.of(item), EMPLOYEE_ID, PIN, PHONE);
        when(orderPersistencePort.getById(1L)).thenReturn(existing);
        when(jwtUtil.getClientIdFromSecurityContext()).thenReturn(CLIENT_ID);
        OrderModel result = useCase.cancelOrder(1L, existing, AUTH);
        assertEquals(OrderStatus.CANCELADO, result.getStatus());
        verify(orderPersistencePort).changeOrderStatus(eq(1L), any());
        verify(traceabilityClientPort).createTrace(any(CreateTraceabilityRequest.class), eq(AUTH));
    }

    @Test
    void getOrderById_nullId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> useCase.getOrderById(null));
    }

    @Test
    void getOrderById_notFound_throwsRuntimeException() {
        when(orderPersistencePort.getUserById(1L)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> useCase.getOrderById(1L));
    }

    @Test
    void getOrderById_found_returnsModel() {
        OrderModel existing = new OrderModel(1L, CLIENT_ID, RESTAURANT_ID, OrderStatus.PENDIENTE, List.of(item), EMPLOYEE_ID, PIN, PHONE);
        when(orderPersistencePort.getUserById(1L)).thenReturn(existing);
        assertSame(existing, useCase.getOrderById(1L));
    }
}
