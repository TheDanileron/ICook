package com.project.icook.model.db

import com.project.icook.model.RecipeDataSource
import com.project.icook.model.data.Recipe
import com.project.icook.model.data.RecipeMapper

class LocalRecipeDataSource(private val recipeDao: RecipeDao) : RecipeDataSource {
    override suspend fun loadRandomRecipes(): Result<List<Recipe>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveRecipe(recipe: Recipe): Result<Long> {
        val id: Long = recipeDao.addRecipe(RecipeMapper.map(recipe))
        return Result.success(id)
    }

    override suspend fun removeRecipe(recipe: Recipe): Result<Int> {
        return Result.success(recipeDao.removeRecipe(RecipeMapper.map(recipe)))
    }

    override suspend fun updateRecipe(recipe: Recipe): Result<Int> {
        return Result.success(recipeDao.updateRecipe(RecipeMapper.map(recipe)))
    }

    override suspend fun loadSavedRecipes(): Result<List<Recipe>> = Result.success(recipeDao.getRecipes().map { RecipeMapper.map(it, emptyList()) })

}