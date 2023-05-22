package com.frcalderon.products.exceptions;

public class IngredientHasProductsAssignedException extends RuntimeException {

    public IngredientHasProductsAssignedException() {
        super("Ingredient has products assigned");
    }
}
