package com.frcalderon.products.controller;

import com.frcalderon.products.controller.dto.IngredientRequest;
import com.frcalderon.products.controller.dto.IngredientResponse;
import com.frcalderon.products.controller.dto.IngredientStockRequest;
import com.frcalderon.products.model.Ingredient;
import com.frcalderon.products.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<IngredientResponse> getAllIngredients() {
        List<Ingredient> ingredients = this.ingredientService.getAllIngredients();
        return ingredients.stream().map(IngredientResponse::new).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public IngredientResponse getIngredient(@PathVariable Long id) {
        Ingredient ingredient = this.ingredientService.getIngredient(id);
        return new IngredientResponse(ingredient);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IngredientResponse createIngredient(@RequestBody IngredientRequest ingredientRequest) {
        Ingredient ingredient = this.ingredientService.createIngredient(ingredientRequest);
        return new IngredientResponse(ingredient);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public IngredientResponse updateIngredient(@PathVariable Long id, @RequestBody IngredientRequest ingredientRequest) {
        Ingredient ingredient = this.ingredientService.updateIngredient(id, ingredientRequest);
        return new IngredientResponse(ingredient);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIngredient(@PathVariable Long id) {
        this.ingredientService.deleteIngredient(id);
    }

    @PostMapping("/stock/add")
    @ResponseStatus(HttpStatus.OK)
    public IngredientResponse addStockToIngredient(@RequestBody IngredientStockRequest ingredientstockRequest) {
        Ingredient ingredient = this.ingredientService.addStockToIngredient(ingredientstockRequest);
        return new IngredientResponse(ingredient);
    }

    @PostMapping("/stock/consume")
    @ResponseStatus(HttpStatus.OK)
    public IngredientResponse consumeStockFromIngredient(@RequestBody IngredientStockRequest ingredientstockRequest) {
        Ingredient ingredient = this.ingredientService.consumeStockFromIngredient(ingredientstockRequest);
        return new IngredientResponse(ingredient);
    }
}
