package com.frcalderon.products.repository;

import com.frcalderon.products.model.Product;
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
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void ProductRepository_GetAll_ReturnMoreThanOneProduct() {
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

        List<Product> productList = productRepository.findAll();

        Assertions.assertThat(productList).isNotNull();
        Assertions.assertThat(productList.size()).isEqualTo(2);
    }

    @Test
    public void ProductRepository_FindById_ReturnProduct() {
        Product product = Product.builder()
                .name("Lotus cheesecake")
                .description("Handmade lotus cheesecake")
                .price(4.5)
                .build();

        productRepository.save(product);

        Product foundProduct = productRepository.findById(product.getId()).get();

        Assertions.assertThat(foundProduct).isNotNull();
    }

    @Test
    public void ProductRepository_Save_ReturnSavedProduct() {
        Product product = Product.builder()
                .name("Lotus cheesecake")
                .description("Handmade lotus cheesecake")
                .price(4.5)
                .build();

        Product savedProduct = productRepository.save(product);

        Assertions.assertThat(savedProduct).isNotNull();
        Assertions.assertThat(savedProduct.getId()).isGreaterThan(0);
    }

    @Test
    public void ProductRepository_Update_ReturnProductNotNull() {
        Product product = Product.builder()
                .name("Lotus cheesecake")
                .description("Handmade lotus cheesecake")
                .price(4.5)
                .build();

        productRepository.save(product);

        Product savedProduct = productRepository.findById(product.getId()).get();
        savedProduct.setName("Oreo cheesecake");
        savedProduct.setDescription("Handmade oreo cheesecake");
        savedProduct.setPrice(5.0);

        Product updatedProduct = productRepository.save(savedProduct);

        Assertions.assertThat(updatedProduct.getName()).isEqualTo(savedProduct.getName());
        Assertions.assertThat(updatedProduct.getDescription()).isEqualTo(savedProduct.getDescription());
        Assertions.assertThat(updatedProduct.getPrice()).isEqualTo(savedProduct.getPrice());
    }

    @Test
    public void ProductRepository_Delete_ReturnProductIsEmpty() {
        Product product = Product.builder()
                .name("Lotus cheesecake")
                .description("Handmade lotus cheesecake")
                .price(4.5)
                .build();

        productRepository.save(product);

        productRepository.deleteById(product.getId());

        Optional<Product> deletedProduct = productRepository.findById(product.getId());

        Assertions.assertThat(deletedProduct).isEmpty();
    }
}
