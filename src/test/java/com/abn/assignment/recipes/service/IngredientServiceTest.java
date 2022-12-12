package com.abn.assignment.recipes.service;

import com.abn.assignment.recipes.dao.IngredientRepository;
import com.abn.assignment.recipes.dto.IngredientDto;
import com.abn.assignment.recipes.enums.MeasurementMetric;
import com.abn.assignment.recipes.model.Ingredient;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@RequiredArgsConstructor
class IngredientServiceTest {

    IngredientService ingredientService;

    @Mock
    IngredientRepository ingredientRepository;

    @BeforeEach
    void setUp() {
        this.ingredientService = new IngredientService(ingredientRepository);
    }

    @Test
    void ingredientDtoToEntity() {
        //when
        IngredientDto ingredientDto = new IngredientDto(1L, "ingredientDto", MeasurementMetric.GM, false);

        //then
        Ingredient ingredient = ingredientService.ingredientDtoToEntity(ingredientDto);

        //assert
        Assertions.assertEquals(ingredientDto.id(), ingredient.getId());
        Assertions.assertEquals(ingredientDto.name(), ingredient.getName());
        Assertions.assertEquals(ingredientDto.isMeat(), ingredient.isMeat());
        Assertions.assertEquals(ingredientDto.measurementMetric(), ingredient.getMeasurementMetric());
    }

    @Test
    void ingredientEntityToDto() {
        //when
        Ingredient ingredient = new Ingredient(1L, "rice", MeasurementMetric.GM, false);

        //then
        IngredientDto ingredientDto = ingredientService.ingredientEntityToDto(ingredient);

        //assert
        Assertions.assertEquals(ingredientDto.id(), ingredient.getId());
        Assertions.assertEquals(ingredientDto.name(), ingredient.getName());
        Assertions.assertEquals(ingredientDto.isMeat(), ingredient.isMeat());
        Assertions.assertEquals(ingredientDto.measurementMetric(), ingredient.getMeasurementMetric());
    }
}
