package com.frcalderon.products.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class Ingredient {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    @Column
    @NotBlank
    @Size(min = 1, max = 10)
    private String units;

    @Column
    private Double stock;

    @OneToMany(mappedBy = "ingredient")
    @JsonIgnore
    private List<ProductIngredient> products;
}
