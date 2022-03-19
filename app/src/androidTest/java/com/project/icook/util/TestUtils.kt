package com.project.icook.util

import com.project.icook.model.data.Ingredient
import com.project.icook.model.data.Recipe

val ingredients = listOf<List<Ingredient>>(
    listOf(Ingredient(0, "Milk", "unit", 10.0), Ingredient(0, "Flour", "unit", 5.0)),
    listOf(Ingredient(0, "Apple", "unit", 5.0), Ingredient(0, "Orange", "unit", 10.0)),
    listOf(Ingredient(0, "Something", "unit", 10.0), Ingredient(0, "Something", "unit", 10.0)),
    listOf(Ingredient(0, "Something", "unit", 10.0), Ingredient(0, "Something", "unit", 10.0)),
    listOf(Ingredient(0, "Something", "unit", 10.0), Ingredient(0, "Something", "unit", 10.0)),
)

val recipes = listOf<Recipe>(
    Recipe(0, "Recipe 1", 10, "noImage", "Recipe 1 summary",
    "Take all ingredients and mix together", false, false, false,ingredients[0]),
    Recipe(0, "Recipe 2", 10, "noImage", "Recipe 2 summary",
        "Take all ingredients and mix together", false, false, false,ingredients[1]),
    Recipe(0, "Recipe 3", 10, "noImage", "Recipe 3 summary",
        "Take all ingredients and mix together", false, false, false,ingredients[2]),
    Recipe(0, "Recipe 4", 10, "noImage", "Recipe4 summary",
        "Take all ingredients and mix together", false, false, false,ingredients[4]),
    Recipe(0, "Recipe 5", 10, "noImage", "Recipe 5 summary",
        "Take all ingredients and mix together", false, false, false,ingredients[4]))


