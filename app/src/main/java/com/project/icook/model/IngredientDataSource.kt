package com.project.icook.model

import com.project.icook.model.data.Ingredient
import com.project.icook.model.db.IngredientLocal

interface IngredientDataSource {
    suspend fun saveIngredients(list: List<Ingredient>, recipeId: Long)

    suspend fun deleteIngredients(list: List<Ingredient>)
}