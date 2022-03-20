package com.project.icook.model

import com.project.icook.model.db.LocalRecipeAndIngredientsRelation

interface RecipeIngredientDataSource {
    // there are two categories of local recipes: cached for when the network goes off and saved recipes
    suspend fun getRecipeIngredientList(isTemp: Boolean): Result<List<LocalRecipeAndIngredientsRelation>>

    suspend fun getRecipeIngredient(id: Long): Result<LocalRecipeAndIngredientsRelation>
}