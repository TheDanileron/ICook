package com.project.icook.model

import com.project.icook.model.data.RecipePreview

interface RecipeDataSource {

    suspend fun loadRandomRecipes() : Result<List<RecipePreview>>
}