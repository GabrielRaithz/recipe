package com.abn.assignment.recipes.dao;

import com.abn.assignment.recipes.model.Ingredient;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface IngredientRepository  extends CrudRepository<Ingredient, Long> {
}
