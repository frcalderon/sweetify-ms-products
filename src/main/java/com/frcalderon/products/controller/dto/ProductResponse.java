package com.frcalderon.products.controller.dto;

import com.frcalderon.products.model.Product;
import com.frcalderon.products.model.ProductIngredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.ingredients = product.getIngredients();
    }

    private Long id;

    private String name;

    private String description;

    private Double price;

    private List<ProductIngredient> ingredients;
}
