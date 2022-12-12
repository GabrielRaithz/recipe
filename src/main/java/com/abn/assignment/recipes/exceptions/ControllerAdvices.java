package com.abn.assignment.recipes.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@ControllerAdvice(basePackages = "com.abn.assignment.recipes")
public class ControllerAdvices {

    @ResponseBody
    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<MessageExceptionHandler> recipeNotFound(RecipeNotFoundException recipeNotFoundException) {
        MessageExceptionHandler error = new MessageExceptionHandler(new Date(), HttpStatus.NOT_FOUND.value(), "Recipe not found");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(IngredientNotFoundException.class)
    public ResponseEntity<MessageExceptionHandler> ingredientNotFound(IngredientNotFoundException ingredientNotFoundException) {
        MessageExceptionHandler error = new MessageExceptionHandler(new Date(), HttpStatus.NOT_FOUND.value(), "Ingredient not found");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<MessageExceptionHandler> argumentsNotValid(ConstraintViolationException notValidException) {
        StringBuilder sb = new StringBuilder();
        notValidException.getConstraintViolations().forEach(fieldError -> {
            sb.append(fieldError.getMessage());
            sb.append(" -> ");
            sb.append(fieldError.getLeafBean());
        });

        MessageExceptionHandler error = new MessageExceptionHandler(new Date(), HttpStatus.BAD_REQUEST.value(), sb.toString());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}




