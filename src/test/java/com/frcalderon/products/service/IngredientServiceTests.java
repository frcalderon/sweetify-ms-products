package com.frcalderon.products.service;

import com.frcalderon.products.controller.dto.IngredientRequest;
import com.frcalderon.products.controller.dto.IngredientStockRequest;
import com.frcalderon.products.exceptions.IngredientHasProductsAssignedException;
import com.frcalderon.products.exceptions.IngredientNotFoundException;
import com.frcalderon.products.model.Ingredient;
import com.frcalderon.products.model.Product;
import com.frcalderon.products.model.ProductIngredient;
import com.frcalderon.products.repository.IngredientRepository;
import com.frcalderon.products.repository.ProductIngredientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class IngredientServiceTests {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private ProductIngredientRepository productIngredientRepository;

    @InjectMocks
    private IngredientService ingredientService;

    private AutoCloseable closeable;

    private Ingredient ingredient;

    private List<Ingredient> ingredientList;

    private Product product;

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

        ingredientList = new ArrayList<>();
        ingredientList.add(ingredient);

        product = Product.builder()
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
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void IngredientService_GetAll_ReturnIngredientList() {
        when(ingredientRepository.findAll()).thenReturn(ingredientList);

        List<Ingredient> result = ingredientService.getAllIngredients();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(ingredient, result.get(0));

        verify(ingredientRepository, times(1)).findAll();
    }

    @Test
    public void IngredientService_FindById_ReturnIngredient() {
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));

        Ingredient result = ingredientService.getIngredient(1L);

        Assertions.assertEquals(ingredient, result);

        verify(ingredientRepository, times(1)).findById(1L);
    }

    @Test
    public void IngredientService_FindById_ReturnIngredientNotFoundException() {
        when(ingredientRepository.findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(IngredientNotFoundException.class, () -> ingredientService.getIngredient(2L));

        verify(ingredientRepository, times(1)).findById(2L);
    }

    @Test
    public void IngredientService_Create_ReturnIngredient() {
        IngredientRequest request = IngredientRequest.builder()
                .name("New Ingredient")
                .units("g")
                .stock(5.0)
                .build();

        Ingredient newIngredient = Ingredient.builder()
                .name(request.getName())
                .units(request.getUnits())
                .stock(request.getStock())
                .build();

        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(newIngredient);

        Ingredient result = ingredientService.createIngredient(request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("New Ingredient", result.getName());
        Assertions.assertEquals("g", result.getUnits());
        Assertions.assertEquals(5.0, result.getStock());

        verify(ingredientRepository, times(1)).save(any(Ingredient.class));
    }

    @Test
    public void IngredientService_Update_ReturnIngredient() {
        IngredientRequest request = IngredientRequest.builder()
                .name("Updated Ingredient")
                .units("g")
                .stock(20.0)
                .build();

        Ingredient updatedIngredient = Ingredient.builder()
                .name(request.getName())
                .units(request.getUnits())
                .stock(request.getStock())
                .build();

        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(updatedIngredient);

        Ingredient result = ingredientService.updateIngredient(1L, request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Updated Ingredient", result.getName());
        Assertions.assertEquals("g", result.getUnits());
        Assertions.assertEquals(20.0, result.getStock());

        verify(ingredientRepository, times(1)).findById(1L);
        verify(ingredientRepository, times(1)).save(any(Ingredient.class));
    }

    @Test
    public void IngredientService_Update_ReturnIngredientNotFoundException() {
        IngredientRequest request = IngredientRequest.builder()
                .name("Updated Ingredient")
                .units("g")
                .stock(20.0)
                .build();

        when(ingredientRepository.findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(IngredientNotFoundException.class, () -> ingredientService.updateIngredient(2L, request));

        verify(ingredientRepository, times(1)).findById(2L);
    }

    @Test
    public void IngredientService_Delete_ReturnVoid() {
        when(ingredientRepository.existsById(1L)).thenReturn(true);

        ingredientService.deleteIngredient(1L);

        verify(ingredientRepository, times(1)).existsById(1L);
        verify(ingredientRepository, times(1)).deleteById(1L);
    }

    @Test
    public void IngredientService_Delete_ReturnIngredientNotFoundException() {
        when(ingredientRepository.existsById(2L)).thenReturn(false);

        Assertions.assertThrows(IngredientNotFoundException.class, () -> ingredientService.deleteIngredient(2L));

        verify(ingredientRepository, times(1)).existsById(2L);
        verify(ingredientRepository, times(0)).deleteById(2L);
    }

    @Test
    public void IngredientService_Delete_ReturnIngredientHasProductsAssignedException() {
        when(ingredientRepository.existsById(1L)).thenReturn(true);
        when(productIngredientRepository.findAllByIngredientId(1L)).thenReturn(productIngredientList);

        Assertions.assertThrows(IngredientHasProductsAssignedException.class, () -> ingredientService.deleteIngredient(1L));

        verify(ingredientRepository, times(1)).existsById(1L);
        verify(productIngredientRepository, times(1)).findAllByIngredientId(1L);
        verify(ingredientRepository, times(0)).deleteById(1L);
    }

    @Test
    public void IngredientService_AddStock_ReturnIngredient() {
        IngredientStockRequest request = IngredientStockRequest.builder()
                .ingredientId(1L)
                .stock(20.0)
                .build();

        Ingredient updatedIngredient = Ingredient.builder()
                .name(ingredient.getName())
                .units(ingredient.getUnits())
                .stock(ingredient.getStock() + request.getStock())
                .build();

        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(updatedIngredient);

        Ingredient result = ingredientService.addStockToIngredient(request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Butter", result.getName());
        Assertions.assertEquals("kg", result.getUnits());
        Assertions.assertEquals(30.0, result.getStock());

        verify(ingredientRepository, times(1)).findById(1L);
        verify(ingredientRepository, times(1)).save(any(Ingredient.class));
    }

    @Test
    public void IngredientService_ConsumeStock_ReturnIngredient() {
        IngredientStockRequest request = IngredientStockRequest.builder()
                .ingredientId(1L)
                .stock(5.0)
                .build();

        Ingredient updatedIngredient = Ingredient.builder()
                .name(ingredient.getName())
                .units(ingredient.getUnits())
                .stock(ingredient.getStock() - request.getStock())
                .build();

        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(updatedIngredient);

        Ingredient result = ingredientService.consumeStockFromIngredient(request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Butter", result.getName());
        Assertions.assertEquals("kg", result.getUnits());
        Assertions.assertEquals(5.0, result.getStock());

        verify(ingredientRepository, times(1)).findById(1L);
        verify(ingredientRepository, times(1)).save(any(Ingredient.class));
    }
}