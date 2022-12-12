package com.abn.assignment.recipes;

import com.abn.assignment.recipes.dto.IngredientDto;
import com.abn.assignment.recipes.enums.MeasurementMetric;
import com.abn.assignment.recipes.dto.IngredientDtoList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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

@SpringBootApplication
@SpringBootTest(classes = IngredientController.class, webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class IngredientControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String url;

    @BeforeEach
    void setUp() {
        url = "http://localhost:" + port + "/api/ingredient";

        IngredientDto rice = new IngredientDto(null, "rice", MeasurementMetric.GM, false);
        IngredientDto beans = new IngredientDto(null, "beans", MeasurementMetric.GM, false);
        IngredientDto beef = new IngredientDto(null, "beef", MeasurementMetric.GM, true);
        IngredientDto egg = new IngredientDto(null, "egg", MeasurementMetric.GM, false);

        ResponseEntity<IngredientDtoList> listResponseEntity = restTemplate.postForEntity(url+"/list",
                List.of(rice, beans, beef, egg), IngredientDtoList.class);

        Assertions.assertEquals(201, listResponseEntity.getStatusCode().value());
    }

    @AfterEach
    void tearDown() {
        restTemplate.delete(url);
    }

    @Test
    void getIngredientById() {
        //when
        ResponseEntity<IngredientDtoList> forEntity = restTemplate.getRestTemplate().getForEntity(url, IngredientDtoList.class);
        Long ingredientDtoId = Objects.requireNonNull(forEntity.getBody()).getIngredientDtos().get(0).id();
        //then
        IngredientDto ingredientDto = restTemplate.getRestTemplate().getForObject(url + "/" + ingredientDtoId, IngredientDto.class);
        //assert
        assert ingredientDto != null;
        Assertions.assertEquals(ingredientDtoId, ingredientDto.id());
        Assertions.assertEquals("rice", ingredientDto.name());
        Assertions.assertEquals(MeasurementMetric.GM, ingredientDto.measurementMetric());
        Assertions.assertFalse(ingredientDto.isMeat());
    }

    @Test
    void getAllIngredient() {
        //when
        //then
        ResponseEntity<IngredientDtoList> forEntity = restTemplate.getRestTemplate().getForEntity(url, IngredientDtoList.class);
        //assert
        Assertions.assertEquals(200, forEntity.getStatusCode().value());
        IngredientDtoList ingredientDtoList = forEntity.getBody();
        assert ingredientDtoList != null;
        Assertions.assertEquals(4, ingredientDtoList.getIngredientDtos().size());
    }

    @Test
    void updateIngredient() {
        //when
        ResponseEntity<IngredientDtoList> forEntity = restTemplate.getRestTemplate().getForEntity(url, IngredientDtoList.class);
        Long ingredientDtoId = Objects.requireNonNull(forEntity.getBody()).getIngredientDtos().get(0).id();
        //then
        IngredientDto rice = new IngredientDto(null, "white rice", MeasurementMetric.GM, false);
        restTemplate.put(url+"/"+ingredientDtoId, rice);
        IngredientDto ingredientDto = restTemplate.getRestTemplate().getForObject(url+"/"+ingredientDtoId, IngredientDto.class);
        //assert
        assert ingredientDto != null;
        Assertions.assertEquals(ingredientDtoId, ingredientDto.id());
        Assertions.assertEquals("white rice", ingredientDto.name());
    }

    @Test
    void deleteIngredient() {
        //when
        ResponseEntity<IngredientDtoList> forEntity = restTemplate.getRestTemplate().getForEntity(url, IngredientDtoList.class);
        Assertions.assertEquals(200, forEntity.getStatusCode().value());
        IngredientDtoList ingredientDtoList = forEntity.getBody();
        assert ingredientDtoList != null;
        Assertions.assertEquals(4, ingredientDtoList.getIngredientDtos().size());

        //then
        restTemplate.delete(url+"/"+ingredientDtoList.getIngredientDtos().get(0).id());

        //assert
        forEntity = restTemplate.getRestTemplate().getForEntity(url, IngredientDtoList.class);
        Assertions.assertEquals(200, forEntity.getStatusCode().value());
        ingredientDtoList = forEntity.getBody();
        assert ingredientDtoList != null;
        Assertions.assertEquals(3, ingredientDtoList.getIngredientDtos().size());
    }

}
