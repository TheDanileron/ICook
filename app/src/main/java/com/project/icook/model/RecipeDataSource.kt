package com.project.icook.model

import com.project.icook.model.data.Recipe

interface RecipeDataSource {

    suspend fun loadRandomRecipes() : Result<List<Recipe>>

    suspend fun saveRecipe(recipe: Recipe) : Result<Long>

    suspend fun removeRecipe(recipe: Recipe) : Result<Int>

    suspend fun updateRecipe(recipe: Recipe) : Result<Int>

    suspend fun loadSavedRecipes() : Result<List<Recipe>>
}