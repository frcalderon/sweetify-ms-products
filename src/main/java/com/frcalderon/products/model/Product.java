package com.frcalderon.products.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Double price;

    @OneToMany(mappedBy = "product")
    private List<ProductIngredient> ingredients;
}
