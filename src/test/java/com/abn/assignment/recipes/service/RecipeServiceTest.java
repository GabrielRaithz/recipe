package com.abn.assignment.recipes.service;

import com.abn.assignment.recipes.dao.RecipeRepository;
import com.abn.assignment.recipes.dto.IngredientDto;
import com.abn.assignment.recipes.dto.RecipeDto;
import com.abn.assignment.recipes.enums.MeasurementMetric;
import com.abn.assignment.recipes.model.Ingredient;
import com.abn.assignment.recipes.model.Recipe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

class RecipeServiceTest {

    RecipeService recipeService;

    @BeforeEach
    void setUp() {
        recipeService = new RecipeService(Mockito.mock(RecipeRepository.class), Mockito.mock(IngredientService.class));
    }

    @Test
    void filterIngredients() {
        //when
        Ingredient rice = new Ingredient(1L, "rice", MeasurementMetric.GM, true);
        Ingredient beans = new Ingredient(2L, "beans", MeasurementMetric.GM, false);
        Ingredient beef = new Ingredient(3L, "beef", MeasurementMetric.GM, true);

        Recipe recipeRice = new Recipe(1L, "rice", false, 2, new HashSet<>(List.of(rice)), "cook rice", true);
        Recipe recipeBeans = new Recipe(3L, "rice & beans", false, 2, new HashSet<>(Arrays.asList(rice, beans)), "cook beans", true);
        Recipe recipeBeef = new Recipe(3L, "rice & beans & beef", false, 2, new HashSet<>(Arrays.asList(rice, beans, beef)), "cook beef", false);

        List<Ingredient> includeIngredients = List.of(beans);
        List<Ingredient> excludeIngredients = List.of(beef);

        //then
        List<RecipeDto> recipeDtoFiltered = recipeService.filterIngredients(includeIngredients, excludeIngredients, List.of(recipeRice, recipeBeans, recipeBeef));

        //assert
        RecipeDto recipeDto = recipeDtoFiltered.get(0);
        Assertions.assertEquals(recipeBeans.getId(), recipeDto.id());
        Assertions.assertEquals(recipeBeans.getVegetarian(), recipeDto.vegetarian());
        Assertions.assertEquals(recipeBeans.getName(), recipeDto.name());
        Assertions.assertEquals(recipeBeans.getIsFavorite(), recipeDto.isFavorite());
        Assertions.assertEquals(recipeBeans.getInstructions(), recipeDto.instructions());
        Assertions.assertEquals(recipeBeans.getServingsQuantity(), recipeDto.servingsQuantity());
    }

    @Test
    void recipeDtoToEntity() {
        //when
        IngredientDto ingredientDto = new IngredientDto(1L, "frozen bitterballen", MeasurementMetric.GM, false);
        RecipeDto recipeDto = new RecipeDto(1L, "bitterballen", true, 1, List.of(ingredientDto), "put in the oven", true);

        //then
        Recipe recipe = recipeService.recipeDtoToEntity(recipeDto);

        //assert
        Assertions.assertEquals(recipe.getId(), recipeDto.id());
        Assertions.assertEquals(recipe.getName(), recipeDto.name());
        Assertions.assertEquals(recipe.getIsFavorite(), recipeDto.isFavorite());
        Assertions.assertEquals(recipe.getInstructions(), recipeDto.instructions());
        Assertions.assertEquals(recipe.getServingsQuantity(), recipeDto.servingsQuantity());
        Assertions.assertEquals(recipe.getVegetarian(), recipeDto.vegetarian());

        recipe.getIngredients().forEach(ingredient -> {
            Assertions.assertEquals(ingredient.getId(), recipeDto.ingredients().get(0).id());
            Assertions.assertEquals(ingredient.getName(), recipeDto.ingredients().get(0).name());
            Assertions.assertEquals(ingredient.getMeasurementMetric(), recipeDto.ingredients().get(0).measurementMetric());
        });
    }

    @Test
    void recipeEntityToDto() {
        //when
        Set<Ingredient> ingredientSet = new HashSet<>();
        ingredientSet.add(new Ingredient(1L, "frozen bitterballen", MeasurementMetric.GM, true));
        Recipe recipe = new Recipe(1L, "bitterballen", false, 2, ingredientSet, "put in the oven", false);

        //then
        RecipeDto recipeDto = recipeService.recipeEntityToDto(recipe);

        //assert
        Assertions.assertEquals(recipe.getId(), recipeDto.id());
        Assertions.assertEquals(recipe.getName(), recipeDto.name());
        Assertions.assertEquals(recipe.getIsFavorite(), recipeDto.isFavorite());
        Assertions.assertEquals(recipe.getInstructions(), recipeDto.instructions());
        Assertions.assertEquals(recipe.getServingsQuantity(), recipeDto.servingsQuantity());
        Assertions.assertEquals(recipe.getVegetarian(), recipeDto.vegetarian());
        recipe.getIngredients().forEach(ingredient -> {
            Assertions.assertEquals(ingredient.getId(), recipeDto.ingredients().get(0).id());
            Assertions.assertEquals(ingredient.getName(), recipeDto.ingredients().get(0).name());
            Assertions.assertEquals(ingredient.getMeasurementMetric(), recipeDto.ingredients().get(0).measurementMetric());
        });
    }

    @Test
    void ingredientDtoToEntity() {
        //when
        IngredientDto ingredientDto = new IngredientDto(1L, "ingredientDto", MeasurementMetric.GM, false);

        //then
        Ingredient ingredient = recipeService.ingredientDtoToEntity(ingredientDto);

        //assert
        Assertions.assertEquals(ingredientDto.id(), ingredient.getId());
        Assertions.assertEquals(ingredientDto.name(), ingredient.getName());
        Assertions.assertEquals(ingredientDto.isMeat(), ingredient.isMeat());
        Assertions.assertEquals(ingredientDto.measurementMetric(), ingredient.getMeasurementMetric());
    }
}
