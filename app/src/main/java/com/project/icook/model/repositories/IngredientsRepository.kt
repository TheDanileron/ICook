package com.project.icook.model.repositories

import com.project.icook.model.db.IngredientLocal
import com.project.icook.model.db.LocalIngredientDataSource

class IngredientsRepository(val localIngredientsDataSource: LocalIngredientDataSource) {
    suspend fun saveIngredients(list: List<IngredientLocal>) {
         localIngredientsDataSource.saveIngredients(list)
    }

    suspend fun deleteIngredients(list: List<IngredientLocal>) {
         localIngredientsDataSource.deleteIngredients(list)
    }
}