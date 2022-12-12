package com.abn.assignment.recipes.service;

import com.abn.assignment.recipes.dao.RecipeRepository;
import com.abn.assignment.recipes.exceptions.RecipeNotFoundException;
import com.abn.assignment.recipes.model.Ingredient;
import com.abn.assignment.recipes.dto.IngredientDto;
import com.abn.assignment.recipes.model.Recipe;
import com.abn.assignment.recipes.dto.RecipeDto;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientService ingredientService;

    public RecipeService(RecipeRepository recipeRepository, IngredientService ingredientService) {
        this.recipeRepository = recipeRepository;
        this.ingredientService = ingredientService;
    }

    public RecipeDto createRecipe(RecipeDto recipeDto) {
        Recipe savedRecipe = recipeRepository.save(recipeDtoToEntity(recipeDto));
        return recipeEntityToDto(savedRecipe);
    }

    public RecipeDto updateRecipe(Long recipeId, RecipeDto recipeDto) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(RecipeNotFoundException::new);
        recipe.setName(recipeDto.name());
        recipe.setIsFavorite(recipeDto.isFavorite());
        recipe.setIngredients(new HashSet<>(recipeDto.ingredients().stream().map(this::ingredientDtoToEntity).toList()));
        recipe.setVegetarian(recipe.getIngredients().stream().noneMatch(Ingredient::isMeat));
        recipe.setInstructions(recipeDto.instructions());
        return recipeEntityToDto(recipeRepository.save(recipe));
    }

    public List<RecipeDto> getAllRecipes() {
        return this.recipeRepository.findAll().stream().map(this::recipeEntityToDto).toList();
    }

    public RecipeDto getRecipeById(Long id) {
        return recipeEntityToDto(this.recipeRepository.findById(id).orElseThrow(RecipeNotFoundException::new));
    }

    public List<RecipeDto> findRecipe(Boolean isVegetarian,
                                      Integer numberOfServings,
                                      List<Long> includeIngredientId,
                                      List<Long> excludeIngredientId,
                                      String instructions
    ) {
        List<Ingredient> includeIngredients = new ArrayList<>();
        List<Ingredient> excludeIngredients = new ArrayList<>();
        if(includeIngredientId != null)
            includeIngredients = ingredientService.getIngredientByIds(includeIngredientId).stream().map(this::ingredientDtoToEntity).toList();
        if(excludeIngredientId != null)
            excludeIngredients = ingredientService.getIngredientByIds(excludeIngredientId).stream().map(this::ingredientDtoToEntity).toList();

        ExampleMatcher matcher = ExampleMatcher
                .matchingAll()
                .withMatcher("instructions", contains().ignoreCase());

        Recipe recipe = Recipe.builder()
                .vegetarian(isVegetarian)
                .servingsQuantity(numberOfServings)
                .instructions(instructions)
                .build();

        List<Recipe> foundedRecipes = recipeRepository.findAll(Example.of(recipe, matcher));

        return filterIngredients(includeIngredients, excludeIngredients, foundedRecipes);
    }


    public void deleteRecipe(Long id) {
        this.recipeRepository.deleteById(id);
    }

    protected List<RecipeDto> filterIngredients(List<Ingredient> includeIngredients, List<Ingredient> excludeIngredients, List<Recipe> foundedRecipes) {
        foundedRecipes = foundedRecipes.stream().filter(foundedRecipe -> foundedRecipe.getIngredients().containsAll(includeIngredients)).toList();
        foundedRecipes = foundedRecipes.stream().filter(foundedRecipe -> Collections.disjoint(foundedRecipe.getIngredients(), excludeIngredients)).toList();
        return foundedRecipes.stream().map(this::recipeEntityToDto).toList();
    }

    //Mappers
    protected Recipe recipeDtoToEntity(RecipeDto recipeDto) {
        List<Ingredient> ingredients = recipeDto.ingredients().stream().map(this::ingredientDtoToEntity).toList();

        boolean hasMeat = ingredients.stream().anyMatch(Ingredient::isMeat);

        return new Recipe(recipeDto.id(),
                recipeDto.name(),
                recipeDto.isFavorite(),
                recipeDto.servingsQuantity(),
                new HashSet<>(ingredients),
                recipeDto.instructions(),
                !hasMeat);
    }

    protected RecipeDto recipeEntityToDto(Recipe recipe) {
        List<IngredientDto> ingredientDtoList = new ArrayList<>();
        recipe.getIngredients().forEach(ingredient -> ingredientDtoList.add(
                new IngredientDto(ingredient.getId(),
                        ingredient.getName(),
                        ingredient.getMeasurementMetric(),
                        ingredient.isMeat())
        ));
        return new RecipeDto(recipe.getId(),
                recipe.getName(),
                recipe.getIsFavorite(),
                recipe.getServingsQuantity(),
                ingredientDtoList,
                recipe.getInstructions(),
                recipe.getVegetarian());
    }

    protected Ingredient ingredientDtoToEntity(IngredientDto ingredientDto) {
        return new Ingredient(ingredientDto.id(), ingredientDto.name(), ingredientDto.measurementMetric(), ingredientDto.isMeat());
    }

    public void deleteAllRecipes() {
        recipeRepository.deleteAll();
    }
}
