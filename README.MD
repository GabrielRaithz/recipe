

-> Two Entities were created, Ingredient.class and Recipe.class
-> Recipe contains Ingredients


# Create list of ingredients

## URL: http://localhost:8080/api/ingredient/list

Ingredient JSON example for creation:

```[{
    "name": "rice",
    "measurementMetric": "GM",
    "isMeat": false
},
{
    "name": "beans",
    "measurementMetric": "GM",
    "isMeat": false
}]```

-> Once we have ingredients created we can create a recipe

# Recipe JSON example:

## URL: http://localhost:8080/api/recipe

```{
    "name": "beans cooked",
    "isFavorite": true,
    "servingsQuantity": 4,
    "ingredients": [
        {
            "id": 1,
            "name": "rice",
            "measurementMetric": "GM",
            "isMeat": false
        ,
        {
            "id": 2,
            "name": "beans",
            "measurementMetric": "GM",
            "isMeat": false
        }
    ],
    "instructions": "boil and fry",
    "vegetarian": true
}```


-> For the search we have the following parameters

## URL: http://localhost:8080/api/recipe/search
### Query parameters:
- numberOfServings: number
- isVegetarian: boolean
- includeIngredientsId: [ids]
- excludeIngredientsId: [ids]
-instructions: String

