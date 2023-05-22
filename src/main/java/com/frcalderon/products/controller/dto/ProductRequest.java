package com.frcalderon.products.controller.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductRequest {

    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    @NotBlank
    @Size(min = 1, max = 250)
    private String description;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    private Double price;

    @NotNull
    private List<ProductIngredientRequest> productIngredientList;
}
