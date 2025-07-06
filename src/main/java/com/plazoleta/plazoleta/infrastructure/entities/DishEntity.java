package com.plazoleta.plazoleta.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "dishes")
@NoArgsConstructor
@AllArgsConstructor
public class DishEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String name;
    Integer price;
    String description;
    String urlImagen;
    String category;
    Long restaurantId;
    boolean isActive = true;
}
