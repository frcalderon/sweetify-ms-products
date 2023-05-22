package com.frcalderon.products.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frcalderon.products.controller.dto.ManageProductsRequest;
import com.frcalderon.products.controller.dto.ProductIngredientRequest;
import com.frcalderon.products.controller.dto.ProductRequest;
import com.frcalderon.products.controller.dto.ProductResponse;
import com.frcalderon.products.model.Ingredient;
import com.frcalderon.products.model.Product;
import com.frcalderon.products.model.ProductIngredient;
import com.frcalderon.products.service.ProductService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product product;

    private ProductRequest productRequest;

    private ProductResponse productResponse;


    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("Lotus cheesecake")
                .description("Handmade Lotus cheesecake")
                .price(4.5)
                .build();
        Ingredient ingredient = Ingredient.builder()
                .id(1L)
                .name("Butter")
                .units("kg")
                .stock(5.5)
                .build();
        ProductIngredient productIngredient = ProductIngredient.builder()
                .product(product)
                .ingredient(ingredient)
                .quantity(0.5)
                .build();
        product.setIngredients(Collections.singletonList(productIngredient));
        productRequest = ProductRequest.builder()
                .name("Lotus cheesecake")
                .description("Handmade Lotus cheesecake")
                .price(4.5)
                .productIngredientList(Collections.singletonList(
                        ProductIngredientRequest.builder().ingredientId(1L).quantity(0.5).build()
                )).build();
        productResponse = ProductResponse.builder()
                .name("Lotus cheesecake")
                .description("Handmade Lotus cheesecake")
                .price(4.5)
                .ingredients(Collections.singletonList(
                        ProductIngredient.builder().product(product).ingredient(ingredient).quantity(0.5).build()
                )).build();
    }

    @Test
    public void ProductController_GetAllProducts_ReturnListOfProductResponseAndOk() throws Exception {
        List<Product> productResponseList = Collections.singletonList(product);

        when(productService.getAllProducts()).thenReturn(productResponseList);

        ResultActions response = mockMvc.perform(get("/products")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.size()",
                        CoreMatchers.is(productResponseList.size())
                ));
    }

    @Test
    public void ProductController_GetProduct_ReturnProductResponseAndOk() throws Exception {
        Long productId = 1L;
        when(productService.getProduct(productId)).thenReturn(product);

        ResultActions response = mockMvc.perform(get("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.name",
                        CoreMatchers.is(productResponse.getName())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.description",
                        CoreMatchers.is(productResponse.getDescription())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.price",
                        CoreMatchers.is(productResponse.getPrice())
                ));
    }

    @Test
    public void ProductController_CreateProduct_ReturnProductResponseAndCreated() throws Exception {
        when(productService.createProduct(productRequest)).thenReturn(product);

        ResultActions response = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest))
        );

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.name",
                        CoreMatchers.is(productResponse.getName())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.description",
                        CoreMatchers.is(productResponse.getDescription())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.price",
                        CoreMatchers.is(productResponse.getPrice())
                ));
    }

    @Test
    public void ProductController_UpdateProduct_ReturnProductResponseAndOk() throws Exception {
        Long productId = 1L;
        when(productService.updateProduct(productId, productRequest)).thenReturn(product);

        ResultActions response = mockMvc.perform(put("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest))
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.name",
                        CoreMatchers.is(productResponse.getName())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.description",
                        CoreMatchers.is(productResponse.getDescription())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.price",
                        CoreMatchers.is(productResponse.getPrice())
                ));
    }

    @Test
    public void ProductController_DeleteProduct_ReturnNoContent() throws Exception {
        Long productId = 1L;
        doNothing().when(productService).deleteProduct(productId);

        ResultActions response = mockMvc.perform(delete("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void ProductController_AddStockToProduct_ReturnOk() throws Exception {
        List<ManageProductsRequest> addProductsRequest = Collections.singletonList(
                ManageProductsRequest.builder()
                        .productId(1L)
                        .quantity(2)
                        .build()
        );

        doNothing().when(productService).addProducts(addProductsRequest);

        ResultActions response = mockMvc.perform(post("/products/stock/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addProductsRequest))
        );

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void ProductController_ConsumeStockFromProduct_ReturnOk() throws Exception {
        List<ManageProductsRequest> consumeProductsRequest = Collections.singletonList(
                ManageProductsRequest.builder()
                        .productId(1L)
                        .quantity(2)
                        .build()
        );

        doNothing().when(productService).consumeProducts(consumeProductsRequest);

        ResultActions response = mockMvc.perform(post("/products/stock/consume")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(consumeProductsRequest))
        );

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
