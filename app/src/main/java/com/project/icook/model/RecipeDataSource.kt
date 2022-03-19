package com.project.icook.model

import com.project.icook.model.data.Recipe

interface RecipeDataSource {

    suspend fun loadRandomRecipes() : Result<List<Recipe>>

    suspend fun saveRecipe(recipe: Recipe, isTemp: Boolean = false) : Result<Long>

    suspend fun saveRecipes(recipes: List<Recipe>, isTemp: Boolean = false) : Result<Int>

    suspend fun removeRecipe(recipe: Recipe) : Result<Int>

    suspend fun removeRecipes(recipes: List<Recipe>) : Result<Int>

    suspend fun removeTempRecipes() : Result<Int>

    suspend fun updateRecipe(recipe: Recipe) : Result<Int>

    suspend fun loadSavedRecipes() : Result<List<Recipe>>
}