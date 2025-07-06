package com.plazoleta.plazoleta.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "restaurants")
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String nit;
    private String address;
    private String phoneNumber;
    private String logoUrl;
    private Long ownerId;
}