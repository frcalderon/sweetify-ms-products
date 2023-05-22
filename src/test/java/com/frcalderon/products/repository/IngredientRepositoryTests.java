package com.frcalderon.products.repository;

import com.frcalderon.products.model.Ingredient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class IngredientRepositoryTests {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    public void IngredientRepository_GetAll_ReturnMoreThanOneIngredient() {
        Ingredient ingredient1 = Ingredient.builder()
                .name("Butter")
                .units("kg")
                .stock(2.0)
                .build();

        Ingredient ingredient2 = Ingredient.builder()
                .name("Butter")
                .units("kg")
                .stock(2.0)
                .build();

        ingredientRepository.save(ingredient1);
        ingredientRepository.save(ingredient2);

        List<Ingredient> ingredientList = ingredientRepository.findAll();

        Assertions.assertThat(ingredientList).isNotNull();
        Assertions.assertThat(ingredientList.size()).isEqualTo(2);
    }

    @Test
    public void IngredientRepository_FindById_ReturnIngredient() {
        Ingredient ingredient = Ingredient.builder()
                .name("Butter")
                .units("kg")
                .stock(2.0)
                .build();

        ingredientRepository.save(ingredient);

        Ingredient foundIngredient = ingredientRepository.findById(ingredient.getId()).get();

        Assertions.assertThat(foundIngredient).isNotNull();
    }

    @Test
    public void IngredientRepository_Save_ReturnSavedIngredient() {
        Ingredient ingredient = Ingredient.builder()
                .name("Butter")
                .units("kg")
                .stock(2.0)
                .build();

        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        Assertions.assertThat(savedIngredient).isNotNull();
        Assertions.assertThat(savedIngredient.getId()).isGreaterThan(0);
    }

    @Test
    public void IngredientRepository_Update_ReturnIngredientNotNull() {
        Ingredient ingredient = Ingredient.builder()
                .name("Butter")
                .units("kg")
                .stock(2.0)
                .build();

        ingredientRepository.save(ingredient);

        Ingredient savedIngredient = ingredientRepository.findById(ingredient.getId()).get();
        savedIngredient.setName("Chocolate");
        savedIngredient.setUnits("g");
        savedIngredient.setStock(3000.0);

        Ingredient updatedIngredient = ingredientRepository.save(savedIngredient);

        Assertions.assertThat(updatedIngredient.getName()).isEqualTo(savedIngredient.getName());
        Assertions.assertThat(updatedIngredient.getUnits()).isEqualTo(savedIngredient.getUnits());
        Assertions.assertThat(updatedIngredient.getStock()).isEqualTo(savedIngredient.getStock());
    }

    @Test
    public void IngredientRepository_Delete_ReturnIngredientIsEmpty() {
        Ingredient ingredient = Ingredient.builder()
                .name("Butter")
                .units("kg")
                .stock(2.0)
                .build();

        ingredientRepository.save(ingredient);

        ingredientRepository.deleteById(ingredient.getId());

        Optional<Ingredient> deletedIngredient = ingredientRepository.findById(ingredient.getId());

        Assertions.assertThat(deletedIngredient).isEmpty();
    }
}
