package com.project.icook.model

import com.project.icook.model.db.LocalRecipeAndIngredients

interface RecipeIngredientDataSource {
    suspend fun getRecipeIngredientList(): Result<List<LocalRecipeAndIngredients>>
}