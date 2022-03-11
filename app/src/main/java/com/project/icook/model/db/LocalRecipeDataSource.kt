package com.project.icook.model.db

import com.project.icook.model.RecipeDataSource
import com.project.icook.model.data.RecipePreview

class LocalRecipeDataSource(private val recipeDao: RecipeDao) : RecipeDataSource {
    override suspend fun loadRandomRecipes(): Result<List<RecipePreview>> {
        TODO("Not yet implemented")
    }
}