package com.frcalderon.products.controller.dto;

import com.frcalderon.products.model.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientResponse {

    public IngredientResponse(Ingredient ingredient) {
        this.id = ingredient.getId();
        this.name = ingredient.getName();
        this.units = ingredient.getUnits();
        this.stock = ingredient.getStock();
    }

    private Long id;

    private String name;

    private String units;

    private Double stock;
}
