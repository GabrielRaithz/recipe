package com.abn.assignment.recipes;

import com.abn.assignment.recipes.dto.IngredientDto;
import com.abn.assignment.recipes.dto.IngredientDtoList;
import com.abn.assignment.recipes.service.IngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ingredient")
public class IngredientController {

    private final IngredientService ingredientService;

    @GetMapping("/{ingredientId}")
    @ResponseBody
    public ResponseEntity<IngredientDto> getIngredientById(@PathVariable Long ingredientId) {
        return new ResponseEntity<>(ingredientService.getIngredientById(ingredientId), HttpStatus.OK);
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<IngredientDtoList> getAllIngredient() {
        return new ResponseEntity<>(new IngredientDtoList(ingredientService.getAllIngredients()), HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<IngredientDto> createIngredient(@Valid @RequestBody IngredientDto ingredientDto) {
        return new ResponseEntity<>(ingredientService.createIngredient(ingredientDto), HttpStatus.CREATED);
    }

    @ResponseBody
    @PostMapping("/list")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<IngredientDtoList> createIngredients(@Valid @RequestBody List<IngredientDto> ingredientDto) {
        return new ResponseEntity<>(new IngredientDtoList(ingredientService.createIngredients(ingredientDto)), HttpStatus.CREATED);
    }

    @ResponseBody
    @PutMapping("/{ingredientId}")
    public ResponseEntity<IngredientDto> updateIngredient(@PathVariable Long ingredientId, @Valid @RequestBody IngredientDto ingredientDto)  {
        return new ResponseEntity<>(ingredientService.updateIngredient(ingredientId, ingredientDto), HttpStatus.ACCEPTED);
    }

    @ResponseBody
    @DeleteMapping("/{ingredientId}")
    public void deleteIngredient(@PathVariable Long ingredientId) {
        ingredientService.deleteIngredient(ingredientId);
    }

    @ResponseBody
    @DeleteMapping
    public void deleteAllIngredients() {
        ingredientService.deleteAll();
    }

}
