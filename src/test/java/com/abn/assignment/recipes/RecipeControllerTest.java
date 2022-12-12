package com.abn.assignment.recipes;

import com.abn.assignment.recipes.dto.IngredientDto;
import com.abn.assignment.recipes.dto.IngredientDtoList;
import com.abn.assignment.recipes.dto.RecipeDto;
import com.abn.assignment.recipes.dto.RecipeDtoList;
import com.abn.assignment.recipes.enums.MeasurementMetric;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootApplication
@SpringBootTest(classes = RecipeController.class, webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecipeControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String url;

    @BeforeEach
    void setUp() {
        url = "http://localhost:" + port + "/api/recipe";

        IngredientDto rice = new IngredientDto(1L, "rice", MeasurementMetric.GM, false);
        IngredientDto beans = new IngredientDto(2L, "beans", MeasurementMetric.GM, false);
        IngredientDto beef = new IngredientDto(3L, "beef", MeasurementMetric.GM, true);
        IngredientDto egg = new IngredientDto(4L, "egg", MeasurementMetric.GM, false);

        this.restTemplate.postForEntity("http://localhost:" + port + "/api/ingredient/list",
                List.of(rice, beans, beef, egg), IngredientDtoList.class);

        RecipeDto riceRecipe = new RecipeDto(1L, "rice", false, 3,
                Lists.list(rice), "cook rice", false);
        restTemplate.postForEntity(url, riceRecipe, RecipeDto.class);

        RecipeDto beansRecipe = new RecipeDto(2L, "rice and beans", false, 2,
                Lists.list(rice, beans),  "cook rice and beans", false);
        restTemplate.postForEntity(url, beansRecipe, RecipeDto.class);

        RecipeDto beefRecipe = new RecipeDto(3L, "rice, beans and beef", false, 2,
                Lists.list(rice, beans, beef), "cook rice and beans fry beef", false);
        restTemplate.postForEntity(url, beefRecipe, RecipeDto.class);
    }

    @AfterEach
    void tearDown() {
        restTemplate.delete("http://localhost:" + port + "/api/ingredient/");
        restTemplate.delete(url);

    }

    @Test
    void getAllRecipe() {
        //when
        //then
        ResponseEntity<RecipeDtoList> recipeDtoListResponseEntity =
                restTemplate.getRestTemplate().getForEntity(url, RecipeDtoList.class);
        //assert
        assertEquals(200, recipeDtoListResponseEntity.getStatusCode().value());
    }

    @Test
    void getRecipeById() {
        //when
        ResponseEntity<RecipeDtoList> allRecipes =
                restTemplate.getRestTemplate().getForEntity(url, RecipeDtoList.class);
        //then
        Long RecipeId = Objects.requireNonNull(allRecipes.getBody()).getRecipeDtos().get(0).id();
        ResponseEntity<RecipeDto> recipeDtoListResponseEntity =
            restTemplate.getRestTemplate().getForEntity(url + "/" + RecipeId, RecipeDto.class);
        //assert
        assertEquals(200, recipeDtoListResponseEntity.getStatusCode().value());
    }

    @Test
    void updateRecipe() {
        //when
        IngredientDto rice = new IngredientDto(1L, "cant change name here", MeasurementMetric.GM, false);
        RecipeDto riceRecipe = new RecipeDto(null, "white rice", false, 2,
                Lists.list(rice), "cook rice and beans", false);

        //then
        restTemplate.put(url+"/1", riceRecipe, RecipeDto.class);

        //assert
        ResponseEntity<RecipeDto> recipeDtoListResponseEntity =
                restTemplate.getRestTemplate().getForEntity(url+"/1", RecipeDto.class);
        assertEquals(200, recipeDtoListResponseEntity.getStatusCode().value());
        assertEquals("white rice", Objects.requireNonNull(recipeDtoListResponseEntity.getBody()).name());
    }

    @Test
    void deleteRecipe() {
        //when
        ResponseEntity<RecipeDtoList> recipeDtoListResponseEntity =
                restTemplate.getRestTemplate().getForEntity(url, RecipeDtoList.class);
        assertEquals(3, Objects.requireNonNull(recipeDtoListResponseEntity.getBody()).getRecipeDtos().size());
        //then
        restTemplate.delete(url + "/" + recipeDtoListResponseEntity.getBody().getRecipeDtos().get(0).id());

        //assert
        recipeDtoListResponseEntity =
                restTemplate.getRestTemplate().getForEntity(url, RecipeDtoList.class);
        assertEquals(2, Objects.requireNonNull(recipeDtoListResponseEntity.getBody()).getRecipeDtos().size());
    }

    @Test
    void searchWithoutParameters() {
        //when
        ResponseEntity<RecipeDtoList> exchange = restTemplate.getRestTemplate().getForEntity(url + "/search", RecipeDtoList.class);
        //assert
        assertEquals(3, Objects.requireNonNull(exchange.getBody()).getRecipeDtos().size());
    }

    @Test
    void searchWithParameter_isVegetarian() {
        //when
        ResponseEntity<RecipeDtoList> exchange = restTemplate.getRestTemplate().getForEntity(url + "/search?isVegetarian=true", RecipeDtoList.class);
        //assert
        assertEquals(2, Objects.requireNonNull(exchange.getBody()).getRecipeDtos().size());
    }

    @Test
    void searchWithParameter_numberOfServings() {
        //when
        ResponseEntity<RecipeDtoList> exchange = restTemplate.getRestTemplate().getForEntity(url + "/search?numberOfServings=3", RecipeDtoList.class);
        //assert
        assertEquals(1, Objects.requireNonNull(exchange.getBody()).getRecipeDtos().size());
    }

    @Test
    void searchWithParameter_instructions() {
        //when
        ResponseEntity<RecipeDtoList> exchange = restTemplate.getRestTemplate().getForEntity(url + "/search?instructions=beans", RecipeDtoList.class);
        //assert
        assertEquals(2, Objects.requireNonNull(exchange.getBody()).getRecipeDtos().size());
    }

    @Test
    void searchWithParameter_includeIngredientsId() {
        //when
        ResponseEntity<RecipeDtoList> exchange = restTemplate.getRestTemplate().getForEntity(url + "/search?includeIngredientsId=1,2,3", RecipeDtoList.class);
        //assert
        assertEquals(1, Objects.requireNonNull(exchange.getBody()).getRecipeDtos().size());
    }

    @Test
    void searchWithParameter_excludeIngredientsId() {
        //when
        ResponseEntity<RecipeDtoList> exchange = restTemplate.getRestTemplate().getForEntity(url + "/search?excludeIngredientsId=3", RecipeDtoList.class);
        //assert
        assertEquals(2, Objects.requireNonNull(exchange.getBody()).getRecipeDtos().size());
    }

}
