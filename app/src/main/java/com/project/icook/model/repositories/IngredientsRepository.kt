package com.project.icook.model.repositories

import com.project.icook.model.data.Ingredient
import com.project.icook.model.data.RecipeMapper
import com.project.icook.model.db.IngredientLocal
import com.project.icook.model.db.LocalIngredientDataSource

class IngredientsRepository(val localIngredientsDataSource: LocalIngredientDataSource) {
    suspend fun saveIngredients(list: List<Ingredient>, recipeId: Long) {
        localIngredientsDataSource.saveIngredients(list, recipeId)
    }

    // Not required for now as we delete ingredients when we delete the recipe
    suspend fun deleteIngredients(list: List<Ingredient>) {
         localIngredientsDataSource.deleteIngredients(list)
    }
}