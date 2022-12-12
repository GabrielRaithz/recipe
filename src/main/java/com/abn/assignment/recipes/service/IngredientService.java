package com.abn.assignment.recipes.service;

import com.abn.assignment.recipes.dao.IngredientRepository;
import com.abn.assignment.recipes.exceptions.IngredientNotFoundException;
import com.abn.assignment.recipes.model.Ingredient;
import com.abn.assignment.recipes.dto.IngredientDto;
import org.modelmapper.internal.util.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<IngredientDto> getAllIngredients() {
        List<Ingredient> ingredientList = Lists.from(this.ingredientRepository.findAll().iterator());
        return ingredientList.stream().map(this::ingredientEntityToDto).toList();
    }

    public IngredientDto getIngredientById(Long id) {
        Ingredient ingredient = this.ingredientRepository.findById(id).orElseThrow(IngredientNotFoundException::new);
        return ingredientEntityToDto(ingredient);
    }

    public List<IngredientDto> getIngredientByIds(List<Long> id) {
        List<Ingredient> ingredients = Lists.from(this.ingredientRepository.findAllById(id).iterator());
        return ingredients.stream().map(this::ingredientEntityToDto).toList();
    }

    public IngredientDto createIngredient(IngredientDto ingredientDto) {
        Ingredient ingredientSaved = this.ingredientRepository.save(ingredientDtoToEntity(ingredientDto));
        return ingredientEntityToDto(ingredientSaved);
    }
    public List<IngredientDto> createIngredients(List<IngredientDto> ingredientDtoList) {
        Iterable<Ingredient> ingredients = this.ingredientRepository.saveAll(ingredientDtoList.stream().map(this::ingredientDtoToEntity).toList());
        return Lists.from(ingredients.iterator()).stream().map(this::ingredientEntityToDto).toList();
    }

    public IngredientDto updateIngredient(Long id, IngredientDto ingredientDto) {
        Ingredient ingredientById = this.ingredientRepository.findById(id).orElseThrow(IngredientNotFoundException::new);
        ingredientById.setMeat(ingredientDto.isMeat());
        ingredientById.setName(ingredientDto.name());
        ingredientById.setMeasurementMetric(ingredientDto.measurementMetric());
        Ingredient save = this.ingredientRepository.save(ingredientById);
        return ingredientEntityToDto(save);
    }

    public void deleteIngredient(Long id) {
        this.ingredientRepository.deleteById(id);
    }

    public void deleteAll() {
        this.ingredientRepository.deleteAll();
    }

    protected Ingredient ingredientDtoToEntity(IngredientDto ingredientDto) {
        return new Ingredient(ingredientDto.id(), ingredientDto.name(), ingredientDto.measurementMetric(), ingredientDto.isMeat());
    }

    protected IngredientDto ingredientEntityToDto(Ingredient ingredient) {
        return new IngredientDto(ingredient.getId(), ingredient.getName(), ingredient.getMeasurementMetric(), ingredient.isMeat());
    }

}
