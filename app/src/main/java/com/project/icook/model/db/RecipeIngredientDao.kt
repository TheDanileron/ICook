package com.project.icook.model.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface RecipeIngredientDao {
    @Transaction
    @Query("SELECT * FROM RecipeLocal")
    fun getUsersAndLibraries(): List<LocalRecipeAndIngredients>
}