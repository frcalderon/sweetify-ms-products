package com.frcalderon.products.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frcalderon.products.controller.dto.IngredientRequest;
import com.frcalderon.products.controller.dto.IngredientResponse;
import com.frcalderon.products.controller.dto.IngredientStockRequest;
import com.frcalderon.products.model.Ingredient;
import com.frcalderon.products.service.IngredientService;
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

@WebMvcTest(controllers = IngredientController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class IngredientControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IngredientService ingredientService;

    @Autowired
    private ObjectMapper objectMapper;

    private Ingredient ingredient;

    private IngredientRequest ingredientRequest;

    private IngredientResponse ingredientResponse;

    private IngredientStockRequest ingredientStockRequest;


    @BeforeEach
    void setUp() {
        ingredient = Ingredient.builder().name("Butter").units("kg").stock(5.5).build();
        ingredientRequest = IngredientRequest.builder().name("Butter").units("kg").stock(5.5).build();
        ingredientResponse = IngredientResponse.builder().name("Butter").units("kg").stock(5.5).build();
        ingredientStockRequest = IngredientStockRequest.builder().ingredientId(1L).stock(5.0).build();
    }

    @Test
    public void IngredientController_GetAllIngredients_ReturnListOfIngredientResponseAndOk() throws Exception {
        List<Ingredient> ingredientResponseList = Collections.singletonList(ingredient);

        when(ingredientService.getAllIngredients()).thenReturn(ingredientResponseList);

        ResultActions response = mockMvc.perform(get("/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.size()",
                        CoreMatchers.is(ingredientResponseList.size())
                ));
    }

    @Test
    public void IngredientController_GetIngredient_ReturnIngredientResponseAndOk() throws Exception {
        Long ingredientId = 1L;
        when(ingredientService.getIngredient(ingredientId)).thenReturn(ingredient);

        ResultActions response = mockMvc.perform(get("/ingredients/1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.name",
                        CoreMatchers.is(ingredientResponse.getName())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.units",
                        CoreMatchers.is(ingredientResponse.getUnits())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.stock",
                        CoreMatchers.is(ingredientResponse.getStock())
                ));
    }

    @Test
    public void IngredientController_CreateIngredient_ReturnIngredientResponseAndCreated() throws Exception {
        when(ingredientService.createIngredient(ingredientRequest)).thenReturn(ingredient);

        ResultActions response = mockMvc.perform(post("/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ingredientRequest))
        );

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.name",
                        CoreMatchers.is(ingredientResponse.getName())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.units",
                        CoreMatchers.is(ingredientResponse.getUnits())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.stock",
                        CoreMatchers.is(ingredientResponse.getStock())
                ));
    }

    @Test
    public void IngredientController_UpdateIngredient_ReturnIngredientResponseAndOk() throws Exception {
        Long ingredientId = 1L;
        when(ingredientService.updateIngredient(ingredientId, ingredientRequest)).thenReturn(ingredient);

        ResultActions response = mockMvc.perform(put("/ingredients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ingredientRequest))
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.name",
                        CoreMatchers.is(ingredientResponse.getName())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.units",
                        CoreMatchers.is(ingredientResponse.getUnits())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.stock",
                        CoreMatchers.is(ingredientResponse.getStock())
                ));
    }

    @Test
    public void IngredientController_DeleteIngredient_ReturnNoContent() throws Exception {
        Long ingredientId = 1L;
        doNothing().when(ingredientService).deleteIngredient(ingredientId);

        ResultActions response = mockMvc.perform(delete("/ingredients/1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void IngredientController_AddStockToIngredient_ReturnIngredientResponseAndOk() throws Exception {
        Double updatedStock = ingredient.getStock() + ingredientStockRequest.getStock();
        Ingredient updatedStockIngredient = Ingredient.builder()
                .name(ingredient.getName())
                .units(ingredient.getUnits())
                .stock(updatedStock)
                .build();
        when(ingredientService.addStockToIngredient(ingredientStockRequest)).thenReturn(updatedStockIngredient);

        ResultActions response = mockMvc.perform(post("/ingredients/stock/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ingredientStockRequest))
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.name",
                        CoreMatchers.is(ingredientResponse.getName())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.units",
                        CoreMatchers.is(ingredientResponse.getUnits())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.stock",
                        CoreMatchers.is(updatedStock)
                ));
    }

    @Test
    public void IngredientController_ConsumeStockFromIngredient_ReturnIngredientResponseAndOk() throws Exception {
        Double updatedStock = ingredient.getStock() - ingredientStockRequest.getStock();
        Ingredient updatedStockIngredient = Ingredient.builder()
                .name(ingredient.getName())
                .units(ingredient.getUnits())
                .stock(updatedStock)
                .build();
        when(ingredientService.consumeStockFromIngredient(ingredientStockRequest)).thenReturn(updatedStockIngredient);

        ResultActions response = mockMvc.perform(post("/ingredients/stock/consume")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ingredientStockRequest))
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.name",
                        CoreMatchers.is(ingredientResponse.getName())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.units",
                        CoreMatchers.is(ingredientResponse.getUnits())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.stock",
                        CoreMatchers.is(updatedStock)
                ));
    }
}
