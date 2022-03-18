package com.project.icook.model.db

import com.project.icook.model.IngredientDataSource

class LocalIngredientDataSource(private val ingredientsDao: IngredientsDao): IngredientDataSource {
    override suspend fun saveIngredients(list: List<IngredientLocal>) {
         ingredientsDao.saveIngredients(list)
    }

    override suspend fun deleteIngredients(list: List<IngredientLocal>) {
         ingredientsDao.deleteIngredients(list)
    }

}