package com.abn.assignment.recipes.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

public record RecipeDto(Long id, @NotNull String name, boolean isFavorite, @NotNull int servingsQuantity,
                        @NotNull List<IngredientDto> ingredients, String instructions, @NotNull boolean vegetarian) implements Serializable {
}
