package com.frcalderon.products.controller.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ManageProductsRequest {

    @NotBlank
    private Long productId;

    @NotNull
    @Digits(integer = 10, fraction = 0)
    private Integer quantity;
}
