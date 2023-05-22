package com.frcalderon.products.service;

import com.frcalderon.products.controller.dto.IngredientRequest;
import com.frcalderon.products.controller.dto.IngredientStockRequest;
import com.frcalderon.products.exceptions.IngredientHasProductsAssignedException;
import com.frcalderon.products.exceptions.IngredientNotFoundException;
import com.frcalderon.products.model.Ingredient;
import com.frcalderon.products.repository.IngredientRepository;
import com.frcalderon.products.repository.ProductIngredientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    private final ProductIngredientRepository productIngredientRepository;

    public List<Ingredient> getAllIngredients() {
        return this.ingredientRepository.findAll();
    }

    public Ingredient getIngredient(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(IngredientNotFoundException::new);
    }

    public Ingredient createIngredient(IngredientRequest ingredientRequest) {
        Ingredient ingredient = Ingredient.builder()
                .name(ingredientRequest.getName())
                .units(ingredientRequest.getUnits())
                .stock(ingredientRequest.getStock())
                .build();

        return ingredientRepository.save(ingredient);
    }

    public Ingredient updateIngredient(Long id, IngredientRequest ingredientRequest) {
        Ingredient ingredientToUpdate = ingredientRepository.findById(id)
                .orElseThrow(IngredientNotFoundException::new);

        ingredientToUpdate.setName(ingredientRequest.getName());
        ingredientToUpdate.setUnits(ingredientRequest.getUnits());
        ingredientToUpdate.setStock(ingredientRequest.getStock());

        return ingredientRepository.save(ingredientToUpdate);
    }

    public void deleteIngredient(Long id) {
        if (!ingredientRepository.existsById(id)) {
            throw new IngredientNotFoundException();
        }

        if (productIngredientRepository.findAllByIngredientId(id).size() > 0) {
            throw new IngredientHasProductsAssignedException();
        }

        ingredientRepository.deleteById(id);
    }

    public Ingredient addStockToIngredient(IngredientStockRequest ingredientStockRequest) {
        Ingredient ingredientToUpdate = ingredientRepository.findById(ingredientStockRequest.getIngredientId())
                .orElseThrow(IngredientNotFoundException::new);

        Double newStock = ingredientToUpdate.getStock() + ingredientStockRequest.getStock();
        ingredientToUpdate.setStock(newStock);

        return ingredientRepository.save(ingredientToUpdate);
    }

    public Ingredient consumeStockFromIngredient(IngredientStockRequest ingredientStockRequest) {
        Ingredient ingredientToUpdate = ingredientRepository.findById(ingredientStockRequest.getIngredientId())
                .orElseThrow(IngredientNotFoundException::new);

        Double newStock = ingredientToUpdate.getStock() - ingredientStockRequest.getStock();
        ingredientToUpdate.setStock(newStock);

        return ingredientRepository.save(ingredientToUpdate);
    }
}
