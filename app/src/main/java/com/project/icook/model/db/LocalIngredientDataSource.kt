package com.project.icook.model.db

import com.project.icook.model.IngredientDataSource
import com.project.icook.model.data.Ingredient
import com.project.icook.model.data.RecipeMapper

class LocalIngredientDataSource(private val ingredientsDao: IngredientsDao): IngredientDataSource {
    override suspend fun saveIngredients(list: List<Ingredient>, recipeId: Long) {
         ingredientsDao.saveIngredients(RecipeMapper.map(list, recipeId))
    }

    override suspend fun deleteIngredients(list: List<Ingredient>) {
         ingredientsDao.deleteIngredients(RecipeMapper.map(list, -1))
    }

}