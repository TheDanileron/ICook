package com.project.icook.model.db

import com.project.icook.model.RecipeIngredientDataSource

class LocalRecipeIngredientDataSource(private val dao: RecipeIngredientDao): RecipeIngredientDataSource {
    override suspend fun getRecipeIngredientList(isTemp: Boolean): Result<List<LocalRecipeAndIngredientsRelation>> {
        return Result.success(dao.getRecipesAndIngredients(isTemp))
    }

    override suspend fun getRecipeIngredient(id: Long): Result<LocalRecipeAndIngredientsRelation> {
        return Result.success(dao.getRecipeAndIngredients(id))
    }

}