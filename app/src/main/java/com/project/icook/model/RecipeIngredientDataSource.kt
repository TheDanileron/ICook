package com.project.icook.model

import com.project.icook.model.db.LocalRecipeAndIngredients

interface RecipeIngredientDataSource {
    // there are two categories of local recipes: cached for when the network goes off and saved recipes
    suspend fun getRecipeIngredientList(isCache: Boolean): Result<List<LocalRecipeAndIngredients>>

}