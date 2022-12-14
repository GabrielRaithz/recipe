package com.abn.assignment.recipes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientDtoList implements Serializable {
    private List<IngredientDto> ingredientDtos;
}
