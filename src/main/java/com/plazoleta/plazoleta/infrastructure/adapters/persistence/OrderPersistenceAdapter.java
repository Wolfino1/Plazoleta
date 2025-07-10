package com.plazoleta.plazoleta.infrastructure.adapters.persistence;

import com.plazoleta.plazoleta.domain.models.OrderItemModel;
import com.plazoleta.plazoleta.domain.models.OrderModel;
import com.plazoleta.plazoleta.domain.models.OrderStatus;
import com.plazoleta.plazoleta.domain.ports.out.OrderPersistencePort;
import com.plazoleta.plazoleta.domain.util.constants.DomainConstants;
import com.plazoleta.plazoleta.domain.util.page.PagedResult;
import com.plazoleta.plazoleta.infrastructure.entities.DishEntity;
import com.plazoleta.plazoleta.infrastructure.entities.OrderEntity;
import com.plazoleta.plazoleta.infrastructure.entities.OrderItemEntity;
import com.plazoleta.plazoleta.infrastructure.entities.RestaurantEntity;
import com.plazoleta.plazoleta.infrastructure.mappers.OrderEntityMapper;
import com.plazoleta.plazoleta.infrastructure.mappers.OrderItemEntityMapper;
import com.plazoleta.plazoleta.infrastructure.repositories.mysql.DishRepository;
import com.plazoleta.plazoleta.infrastructure.repositories.mysql.OrderRepository;
import com.plazoleta.plazoleta.infrastructure.repositories.mysql.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderPersistenceAdapter implements OrderPersistencePort {

    private final OrderRepository       orderRepository;
    private final RestaurantRepository  restaurantRepository;
    private final DishRepository dishRepository;
    private final OrderItemEntityMapper itemMapper;
    private final OrderEntityMapper orderEntityMapper;


    @Override
    public boolean existsByClientAndStatusIn(Long clientId, List<OrderStatus> statuses) {
        return orderRepository.existsByClientIdAndStatusIn(clientId, statuses);
    }

    @Override
    public void save(OrderModel orderModel) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setClientId(orderModel.getClientId());
        orderEntity.setStatus(orderModel.getStatus());

        RestaurantEntity rest = restaurantRepository.findById(orderModel.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        orderEntity.setRestaurant(rest);

        for (OrderItemModel itemModel : orderModel.getItems()) {
            OrderItemEntity itemEntity = itemMapper.modelToEntity(itemModel);
            DishEntity dish = dishRepository.findById(itemModel.getDishId())
                    .orElseThrow(() -> new RuntimeException("Dish not found: " + itemModel.getDishId()));
            itemEntity.setDish(dish);
            itemEntity.setOrder(orderEntity);
            orderEntity.getItems().add(itemEntity);
        }
        orderRepository.save(orderEntity);
    }

    @Override
    public PagedResult<OrderModel> getOrdersByFilter(
            Long restaurantId,
            int page,
            int size,
            Long clientId,
            OrderStatus status
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderEntity> pageEnt = orderRepository.findByFilters(
                restaurantId, clientId, status, null, pageable
        );

        List<OrderModel> content = pageEnt.stream()
                .map(entity -> {
                    OrderModel m = orderEntityMapper.entityToModel(entity);
                    List<OrderItemModel> items = entity.getItems().stream()
                            .map(itemEnt -> {
                                OrderItemModel item = new OrderItemModel(
                                        itemEnt.getDish().getId(),
                                        itemEnt.getQuantity()
                                );
                                return item;
                            })
                            .toList();
                    m.setItems(items);
                    return m;
                })
                .toList();

        return new PagedResult<>(
                content,
                pageEnt.getNumber(),
                pageEnt.getSize(),
                pageEnt.getTotalElements()
        );
    }

    @Override
    public void assignOrder(Long id, OrderModel orderModel) {
        OrderEntity existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(DomainConstants.ORDER_NOT_FOUND + id));

        existingOrder.assignEmployee(orderModel.getEmployeeId());
        orderRepository.save(existingOrder);
    }


    @Override
    public OrderModel getById(Long id) {
        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(DomainConstants.ORDER_NOT_FOUND + id));
        return orderEntityMapper.entityToModel(orderEntity);    }
}



