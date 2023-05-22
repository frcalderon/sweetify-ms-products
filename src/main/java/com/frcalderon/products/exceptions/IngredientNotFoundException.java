package com.frcalderon.products.exceptions;

public class IngredientNotFoundException extends RuntimeException {

    public IngredientNotFoundException() {
        super("Ingredient not found");
    }
}
