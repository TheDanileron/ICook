package com.project.icook.model.db

import androidx.room.*

@Dao
interface IngredientsDao {
    @Query("SELECT * FROM IngredientLocal")
    fun getIngredients(): List<IngredientLocal>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveIngredients(list: List<IngredientLocal>)

    @Delete
    fun deleteIngredients(list: List<IngredientLocal>)
}