package com.frcalderon.products.repository;

import com.frcalderon.products.model.Ingredient;
import com.frcalderon.products.model.Product;
import com.frcalderon.products.model.ProductIngredient;
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
public class ProductIngredientRepositoryTests {

    @Autowired
    private ProductIngredientRepository productIngredientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    public void ProductIngredientRepository_Save_ReturnSavedProductIngredient() {
        Ingredient ingredient = Ingredient.builder()
                .name("Butter")
                .units("kg")
                .stock(2.0)
                .build();

        Product product = Product.builder()
                .name("Lotus cheesecake")
                .description("Handmade lotus cheesecake")
                .price(4.5)
                .build();

        ingredientRepository.save(ingredient);
        productRepository.save(product);

        Assertions.assertThat(ingredient.getId()).isGreaterThan(0);
        Assertions.assertThat(product.getId()).isGreaterThan(0);

        ProductIngredient productIngredient = ProductIngredient.builder()
                .product(product)
                .ingredient(ingredient)
                .quantity(0.5)
                .build();

        ProductIngredient savedProductIngredient = productIngredientRepository.save(productIngredient);

        Assertions.assertThat(savedProductIngredient).isNotNull();
        Assertions.assertThat(savedProductIngredient.getId()).isGreaterThan(0);
    }

    @Test
    public void ProductIngredientRepository_GetByIngredient_ReturnMoreThanOneProductIngredient() {
        Ingredient ingredient = Ingredient.builder()
                .name("Butter")
                .units("kg")
                .stock(2.0)
                .build();

        Product product1 = Product.builder()
                .name("Lotus cheesecake")
                .description("Handmade lotus cheesecake")
                .price(4.5)
                .build();

        Product product2 = Product.builder()
                .name("Oreo cheesecake")
                .description("Handmade oreo cheesecake")
                .price(4.5)
                .build();

        productRepository.save(product1);
        productRepository.save(product2);
        ingredientRepository.save(ingredient);

        Assertions.assertThat(product1.getId()).isGreaterThan(0);
        Assertions.assertThat(product2.getId()).isGreaterThan(0);
        Assertions.assertThat(ingredient.getId()).isGreaterThan(0);

        ProductIngredient productIngredient1 = ProductIngredient.builder()
                .product(product1)
                .ingredient(ingredient)
                .quantity(0.5)
                .build();

        ProductIngredient productIngredient2 = ProductIngredient.builder()
                .product(product2)
                .ingredient(ingredient)
                .quantity(0.5)
                .build();

        productIngredientRepository.save(productIngredient1);
        productIngredientRepository.save(productIngredient2);

        Assertions.assertThat(productIngredient1.getId()).isGreaterThan(0);
        Assertions.assertThat(productIngredient2.getId()).isGreaterThan(0);

        List<ProductIngredient> productIngredientList = productIngredientRepository.findAllByIngredientId(ingredient.getId());

        Assertions.assertThat(productIngredientList).isNotNull();
        Assertions.assertThat(productIngredientList.size()).isEqualTo(2);
    }

    @Test
    public void ProductIngredientRepository_GetByProduct_ReturnMoreThanOneProductIngredient() {
        Ingredient ingredient1 = Ingredient.builder()
                .name("Butter")
                .units("kg")
                .stock(2.0)
                .build();

        Ingredient ingredient2 = Ingredient.builder()
                .name("Chocolate")
                .units("kg")
                .stock(3.0)
                .build();

        Product product = Product.builder()
                .name("Oreo cheesecake")
                .description("Handmade oreo cheesecake")
                .price(4.5)
                .build();

        productRepository.save(product);
        ingredientRepository.save(ingredient1);
        ingredientRepository.save(ingredient2);

        Assertions.assertThat(product.getId()).isGreaterThan(0);
        Assertions.assertThat(ingredient1.getId()).isGreaterThan(0);
        Assertions.assertThat(ingredient2.getId()).isGreaterThan(0);

        ProductIngredient productIngredient1 = ProductIngredient.builder()
                .product(product)
                .ingredient(ingredient1)
                .quantity(0.5)
                .build();

        ProductIngredient productIngredient2 = ProductIngredient.builder()
                .product(product)
                .ingredient(ingredient2)
                .quantity(0.5)
                .build();

        productIngredientRepository.save(productIngredient1);
        productIngredientRepository.save(productIngredient2);

        Assertions.assertThat(productIngredient1.getId()).isGreaterThan(0);
        Assertions.assertThat(productIngredient2.getId()).isGreaterThan(0);

        List<ProductIngredient> productIngredientList = productIngredientRepository.findAllByProductId(product.getId());

        Assertions.assertThat(productIngredientList).isNotNull();
        Assertions.assertThat(productIngredientList.size()).isEqualTo(2);
    }

    @Test
    public void ProductIngredientRepository_Delete_ReturnProductIngredientIsEmpty_ProductAndIngredientAreNotRemoved() {
        Ingredient ingredient = Ingredient.builder()
                .name("Butter")
                .units("kg")
                .stock(2.0)
                .build();

        Product product = Product.builder()
                .name("Lotus cheesecake")
                .description("Handmade lotus cheesecake")
                .price(4.5)
                .build();

        ingredientRepository.save(ingredient);
        productRepository.save(product);

        Assertions.assertThat(ingredient.getId()).isGreaterThan(0);
        Assertions.assertThat(product.getId()).isGreaterThan(0);

        ProductIngredient productIngredient = ProductIngredient.builder()
                .product(product)
                .ingredient(ingredient)
                .quantity(0.5)
                .build();

        ProductIngredient savedProductIngredient = productIngredientRepository.save(productIngredient);

        Assertions.assertThat(savedProductIngredient).isNotNull();
        Assertions.assertThat(savedProductIngredient.getId()).isGreaterThan(0);

        productIngredientRepository.deleteById(savedProductIngredient.getId());

        Optional<ProductIngredient> deletedProductIngredient = productIngredientRepository.findById(savedProductIngredient.getId());

        Assertions.assertThat(deletedProductIngredient).isEmpty();

        Optional<Ingredient> deletedIngredient = ingredientRepository.findById(ingredient.getId());
        Assertions.assertThat(deletedIngredient).isNotEmpty();

        Optional<Product> deletedProduct = productRepository.findById(product.getId());
        Assertions.assertThat(deletedProduct).isNotEmpty();
    }

    @Test
    public void ProductIngredientRepository_DeleteByProduct_ReturnProductIngredientIsEmpty_ProductAndIngredientAreNotRemoved() {
        Ingredient ingredient = Ingredient.builder()
                .name("Butter")
                .units("kg")
                .stock(2.0)
                .build();

        Product product = Product.builder()
                .name("Lotus cheesecake")
                .description("Handmade lotus cheesecake")
                .price(4.5)
                .build();

        ingredientRepository.save(ingredient);
        productRepository.save(product);

        Assertions.assertThat(ingredient.getId()).isGreaterThan(0);
        Assertions.assertThat(product.getId()).isGreaterThan(0);

        ProductIngredient productIngredient = ProductIngredient.builder()
                .product(product)
                .ingredient(ingredient)
                .quantity(0.5)
                .build();

        ProductIngredient savedProductIngredient = productIngredientRepository.save(productIngredient);

        Assertions.assertThat(savedProductIngredient).isNotNull();
        Assertions.assertThat(savedProductIngredient.getId()).isGreaterThan(0);

        productIngredientRepository.deleteByProductId(product.getId());

        Optional<ProductIngredient> deletedProductIngredient = productIngredientRepository.findById(savedProductIngredient.getId());

        Assertions.assertThat(deletedProductIngredient).isEmpty();

        Optional<Ingredient> deletedIngredient = ingredientRepository.findById(ingredient.getId());
        Assertions.assertThat(deletedIngredient).isNotEmpty();

        Optional<Product> deletedProduct = productRepository.findById(product.getId());
        Assertions.assertThat(deletedProduct).isNotEmpty();
    }
}
