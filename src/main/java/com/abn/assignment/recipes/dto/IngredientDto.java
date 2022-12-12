package com.abn.assignment.recipes.dto;

import com.abn.assignment.recipes.enums.MeasurementMetric;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record IngredientDto(Long id, @NotNull String name, @NotNull MeasurementMetric measurementMetric,
                            @NotNull boolean isMeat) implements Serializable {
}
