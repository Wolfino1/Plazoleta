package com.plazoleta.plazoleta.infrastructure.entities;

import com.plazoleta.plazoleta.domain.models.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static jakarta.persistence.FetchType.LAZY;

    @Entity
    @Table(name = "orders")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class OrderEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private Long clientId;

        @ManyToOne(fetch = LAZY)
        @JoinColumn(name = "restaurant_id", nullable = false)
        private RestaurantEntity restaurant;

        @Enumerated(EnumType.STRING)
        private OrderStatus status;

        @OneToMany(
                mappedBy = "order",
                cascade = CascadeType.ALL,
                orphanRemoval = true
        )
        private List<OrderItemEntity> items = new ArrayList<>();
        private Long employeeId;

        @Column(name = "phone_number", nullable = false, length = 13)
        private String phoneNumber;


        @Column(name = "pin_security", nullable = false)
        private Integer pinSecurity;

        @PrePersist
        private void generatePinSecurity() {
            this.pinSecurity = ThreadLocalRandom.current().nextInt(1000, 10000);
        }

        public void assignEmployee(Long employeeId) {
            this.employeeId = employeeId;
            this.status     = OrderStatus.EN_PREPARACION;
        }
    }

