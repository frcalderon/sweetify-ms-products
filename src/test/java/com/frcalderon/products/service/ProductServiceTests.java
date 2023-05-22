package com.frcalderon.products.service;

import com.frcalderon.products.controller.dto.IngredientStockRequest;
import com.frcalderon.products.controller.dto.ManageProductsRequest;
import com.frcalderon.products.controller.dto.ProductIngredientRequest;
import com.frcalderon.products.controller.dto.ProductRequest;
import com.frcalderon.products.exceptions.ProductNotFoundException;
import com.frcalderon.products.model.Ingredient;
import com.frcalderon.products.model.Product;
import com.frcalderon.products.model.ProductIngredient;
import com.frcalderon.products.repository.IngredientRepository;
import com.frcalderon.products.repository.ProductIngredientRepository;
import com.frcalderon.products.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private ProductIngredientRepository productIngredientRepository;

    @Mock
    private IngredientService ingredientService;

    @InjectMocks
    private ProductService productService;

    private AutoCloseable closeable;

    private Product product;

    private Ingredient ingredient;

    private List<Product> productList;

    private ProductIngredient productIngredient;

    private List<ProductIngredient> productIngredientList;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        ingredient = Ingredient.builder()
                .id(1L)
                .name("Butter")
                .units("kg")
                .stock(10.0)
                .build();

        product = Product.builder()
                .id(1L)
                .name("Lotus cheesecake")
                .description("Handmade Lotus cheesecake")
                .price(4.5)
                .build();

        productIngredient = ProductIngredient.builder()
                .product(product)
                .ingredient(ingredient)
                .quantity(2.0)
                .build();

        productIngredientList = new ArrayList<>();
        productIngredientList.add(productIngredient);

        product.setIngredients(productIngredientList);

        productList = new ArrayList<>();
        productList.add(product);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void ProductService_GetAll_ReturnProductsList() {
        when(productRepository.findAll()).thenReturn(productList);

        List<Product> result = productService.getAllProducts();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(product, result.get(0));

        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void ProductService_FindById_ReturnProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProduct(1L);

        Assertions.assertEquals(product, result);

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void ProductService_FindById_ReturnProductNotFoundException() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.getProduct(2L));

        verify(productRepository, times(1)).findById(2L);
    }

    @Test
    public void ProductService_Create_ReturnProduct() {
        ProductRequest request = ProductRequest.builder()
                .name("New Product")
                .description("New product description")
                .price(5.0)
                .productIngredientList(Collections.singletonList(
                        ProductIngredientRequest.builder()
                                .ingredientId(ingredient.getId())
                                .quantity(1.5)
                                .build()
                ))
                .build();

        Product newProduct = Product.builder()
                .id(2L)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();

        ProductIngredient newProductIngredient = ProductIngredient.builder()
                .product(newProduct)
                .ingredient(ingredient)
                .quantity(1.5)
                .build();

        when(ingredientRepository.existsById(ingredient.getId())).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(newProduct);
        when(ingredientRepository.findById(ingredient.getId())).thenReturn(Optional.of(ingredient));
        when(productIngredientRepository.save(any(ProductIngredient.class))).thenReturn(newProductIngredient);
        when(productRepository.findById(2L)).thenReturn(Optional.of(newProduct));

        Product result = productService.createProduct(request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("New Product", result.getName());
        Assertions.assertEquals("New product description", result.getDescription());
        Assertions.assertEquals(5.0, result.getPrice());

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void ProductService_Update_ReturnProduct() {
        ProductRequest request = ProductRequest.builder()
                .name("Updated Product")
                .description("Updated product description")
                .price(5.0)
                .productIngredientList(Collections.singletonList(
                        ProductIngredientRequest.builder()
                                .ingredientId(ingredient.getId())
                                .quantity(1.5)
                                .build()
                ))
                .build();

        Product updatedProduct = Product.builder()
                .id(1L)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();

        ProductIngredient newProductIngredient = ProductIngredient.builder()
                .product(updatedProduct)
                .ingredient(ingredient)
                .quantity(1.5)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(ingredientRepository.existsById(ingredient.getId())).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
        when(ingredientRepository.findById(ingredient.getId())).thenReturn(Optional.of(ingredient));
        when(productIngredientRepository.save(any(ProductIngredient.class))).thenReturn(newProductIngredient);
        when(productRepository.findById(1L)).thenReturn(Optional.of(updatedProduct));

        Product result = productService.updateProduct(1L, request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Updated Product", result.getName());
        Assertions.assertEquals("Updated product description", result.getDescription());
        Assertions.assertEquals(5.0, result.getPrice());

        verify(productRepository, times(2)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void ProductService_Update_ReturnProductNotFoundException() {
        ProductRequest request = ProductRequest.builder()
                .name("Updated Product")
                .description("Updated product description")
                .price(5.0)
                .build();

        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(2L, request));

        verify(productRepository, times(1)).findById(2L);
    }

    @Test
    public void ProductService_Delete_ReturnVoid() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    public void ProductService_Delete_ReturnProductNotFoundException() {
        when(productRepository.existsById(2L)).thenReturn(false);

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(2L));

        verify(productRepository, times(1)).existsById(2L);
        verify(productRepository, times(0)).deleteById(2L);
    }

    @Test
    public void ProductService_AddProducts_ReturnVoid() {
        List<ManageProductsRequest> addProductsRequest = Collections.singletonList(
                ManageProductsRequest.builder()
                        .productId(1L)
                        .quantity(2)
                        .build()
        );

        IngredientStockRequest ingredientStockRequest = IngredientStockRequest.builder()
                .ingredientId(1L)
                .stock(2 * productIngredient.getQuantity())
                .build();

        when(productRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.addProducts(addProductsRequest);

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).findById(1L);
        verify(ingredientService, times(1)).addStockToIngredient(ingredientStockRequest);
    }

    @Test
    public void ProductService_AddProducts_ReturnProductNotFoundException() {
        List<ManageProductsRequest> addProductsRequest = Collections.singletonList(
                ManageProductsRequest.builder()
                        .productId(2L)
                        .quantity(2)
                        .build()
        );

        IngredientStockRequest ingredientStockRequest = IngredientStockRequest.builder()
                .ingredientId(2L)
                .stock(2 * productIngredient.getQuantity())
                .build();

        when(productRepository.existsById(2L)).thenReturn(false);

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.addProducts(addProductsRequest));

        verify(productRepository, times(1)).existsById(2L);
        verify(productRepository, times(0)).findById(2L);
        verify(ingredientService, times(0)).addStockToIngredient(ingredientStockRequest);
    }

    @Test
    public void ProductService_ConsumeProducts_ReturnVoid() {
        List<ManageProductsRequest> consumeProductsRequest = Collections.singletonList(
                ManageProductsRequest.builder()
                        .productId(1L)
                        .quantity(2)
                        .build()
        );

        IngredientStockRequest ingredientStockRequest = IngredientStockRequest.builder()
                .ingredientId(1L)
                .stock(2 * productIngredient.getQuantity())
                .build();

        when(productRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.consumeProducts(consumeProductsRequest);

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).findById(1L);
        verify(ingredientService, times(1)).consumeStockFromIngredient(ingredientStockRequest);
    }

    @Test
    public void ProductService_ConsumeProducts_ReturnProductNotFoundException() {
        List<ManageProductsRequest> consumeProductsRequest = Collections.singletonList(
                ManageProductsRequest.builder()
                        .productId(2L)
                        .quantity(2)
                        .build()
        );

        IngredientStockRequest ingredientStockRequest = IngredientStockRequest.builder()
                .ingredientId(2L)
                .stock(2 * productIngredient.getQuantity())
                .build();

        when(productRepository.existsById(2L)).thenReturn(false);

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.consumeProducts(consumeProductsRequest));

        verify(productRepository, times(1)).existsById(2L);
        verify(productRepository, times(0)).findById(2L);
        verify(ingredientService, times(0)).consumeStockFromIngredient(ingredientStockRequest);
    }
}