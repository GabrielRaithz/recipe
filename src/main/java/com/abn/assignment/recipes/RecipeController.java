package com.abn.assignment.recipes;

import com.abn.assignment.recipes.dto.RecipeDto;
import com.abn.assignment.recipes.dto.RecipeDtoList;
import com.abn.assignment.recipes.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    @ResponseBody
    @GetMapping
    public RecipeDtoList getAllRecipe() {
        return new RecipeDtoList(this.recipeService.getAllRecipes());
    }

    @ResponseBody
    @GetMapping("/{recipeId}")
    public RecipeDto getRecipeById(@PathVariable Long recipeId) {
        return this.recipeService.getRecipeById(recipeId);
    }

    @ResponseBody
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeDto createRecipe(@Valid @RequestBody RecipeDto recipeDto) {
        return this.recipeService.createRecipe(recipeDto);
    }

    @ResponseBody
    @PutMapping("/{recipeId}")
    public RecipeDto updateRecipe(@PathVariable Long recipeId, @Valid @RequestBody RecipeDto recipeDto) {
        return this.recipeService.updateRecipe(recipeId, recipeDto);
    }

    @ResponseBody
    @DeleteMapping("/{recipeId}")
    public void deleteRecipe(@PathVariable Long recipeId) {
        this.recipeService.deleteRecipe(recipeId);
    }

    @ResponseBody
    @DeleteMapping
    public void deleteAllRecipes() {
        this.recipeService.deleteAllRecipes();
    }

    @ResponseBody
    @GetMapping("/search")
    public RecipeDtoList search(
            @RequestParam(value = "isVegetarian", required=false) Boolean isVegetarian,
            @RequestParam(value = "numberOfServings", required=false) Integer numberOfServings,
            @RequestParam(value = "includeIngredientsId", required=false) List<Long> includeIngredientsId,
            @RequestParam(value = "excludeIngredientsId", required=false) List<Long> excludeIngredientsId,
            @RequestParam(value = "instructions", required=false) String instructions
            ) {
        return new RecipeDtoList(this.recipeService.findRecipe(isVegetarian, numberOfServings, includeIngredientsId, excludeIngredientsId, instructions));
    }

}
