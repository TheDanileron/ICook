package com.project.icook.model

import com.project.icook.model.db.IngredientLocal

interface IngredientDataSource {
    suspend fun saveIngredients(list: List<IngredientLocal>)

    suspend fun deleteIngredients(list: List<IngredientLocal>)
}