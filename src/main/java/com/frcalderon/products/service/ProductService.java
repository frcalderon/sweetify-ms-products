package com.frcalderon.products.service;

import com.frcalderon.products.controller.dto.IngredientStockRequest;
import com.frcalderon.products.controller.dto.ManageProductsRequest;
import com.frcalderon.products.controller.dto.ProductIngredientRequest;
import com.frcalderon.products.controller.dto.ProductRequest;
import com.frcalderon.products.exceptions.IngredientNotFoundException;
import com.frcalderon.products.exceptions.ProductNotFoundException;
import com.frcalderon.products.model.Product;
import com.frcalderon.products.model.ProductIngredient;
import com.frcalderon.products.repository.IngredientRepository;
import com.frcalderon.products.repository.ProductIngredientRepository;
import com.frcalderon.products.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductIngredientRepository productIngredientRepository;

    private final IngredientRepository ingredientRepository;

    private final IngredientService ingredientService;

    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    public Product createProduct(ProductRequest productRequest) {
        for (ProductIngredientRequest productIngredient : productRequest.getProductIngredientList()) {
            if (!ingredientRepository.existsById(productIngredient.getIngredientId())) {
                throw new IngredientNotFoundException();
            }
        }

        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        Product productSaved = productRepository.save(product);

        for (ProductIngredientRequest productIngredient : productRequest.getProductIngredientList()) {
            productIngredientRepository.save(
                    ProductIngredient.builder()
                            .product(productSaved)
                            .ingredient(ingredientRepository.findById(productIngredient.getIngredientId()).get())
                            .quantity(productIngredient.getQuantity())
                            .build()
            );
        }

        return productRepository.findById(productSaved.getId()).get();
    }

    @Transactional
    public Product updateProduct(Long id, ProductRequest productRequest) {
        Product productToUpdate = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        for (ProductIngredientRequest productIngredient : productRequest.getProductIngredientList()) {
            if (!ingredientRepository.existsById(productIngredient.getIngredientId())) {
                throw new IngredientNotFoundException();
            }
        }

        productIngredientRepository.deleteByProductId(id);

        for (ProductIngredientRequest productIngredient : productRequest.getProductIngredientList()) {
            productIngredientRepository.save(
                    ProductIngredient.builder()
                            .product(productToUpdate)
                            .ingredient(ingredientRepository.findById(productIngredient.getIngredientId()).get())
                            .quantity(productIngredient.getQuantity())
                            .build()
            );
        }

        productToUpdate.setName(productRequest.getName());
        productToUpdate.setDescription(productRequest.getDescription());
        productToUpdate.setPrice(productRequest.getPrice());

        productRepository.save(productToUpdate);

        return productRepository.findById(id).get();
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException();
        }

        productIngredientRepository.deleteByProductId(id);

        productRepository.deleteById(id);
    }

    public void addProducts(List<ManageProductsRequest> addProductsRequestList) {
        for (ManageProductsRequest product : addProductsRequestList) {
            if (!productRepository.existsById(product.getProductId())) {
                throw new ProductNotFoundException();
            }
        }

        for (ManageProductsRequest product : addProductsRequestList) {
            Product productToAdd = productRepository.findById(product.getProductId()).get();
            for (ProductIngredient productIngredient : productToAdd.getIngredients()) {
                ingredientService.addStockToIngredient(
                        IngredientStockRequest.builder()
                                .ingredientId(productIngredient.getIngredient().getId())
                                .stock(productIngredient.getQuantity() * product.getQuantity())
                                .build()
                );
            }
        }
    }

    public void consumeProducts(List<ManageProductsRequest> consumeProductsRequestList) {
        for (ManageProductsRequest product : consumeProductsRequestList) {
            if (!productRepository.existsById(product.getProductId())) {
                throw new ProductNotFoundException();
            }
        }

        for (ManageProductsRequest product : consumeProductsRequestList) {
            Product productToConsume = productRepository.findById(product.getProductId()).get();
            for (ProductIngredient productIngredient : productToConsume.getIngredients()) {
                ingredientService.consumeStockFromIngredient(
                        IngredientStockRequest.builder()
                                .ingredientId(productIngredient.getIngredient().getId())
                                .stock(productIngredient.getQuantity() * product.getQuantity())
                                .build()
                );
            }
        }
    }
}
