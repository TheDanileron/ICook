package com.project.icook.model.db

import com.project.icook.model.RecipeIngredientDataSource

class LocalRecipeIngredientDataSource(private val dao: RecipeIngredientDao): RecipeIngredientDataSource {
    override suspend fun getRecipeIngredientList(): Result<List<LocalRecipeAndIngredients>> {
        return Result.success(dao.getUsersAndLibraries())
    }

}