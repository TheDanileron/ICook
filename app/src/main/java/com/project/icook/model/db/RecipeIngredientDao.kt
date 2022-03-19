package com.project.icook.model.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface RecipeIngredientDao {
    // this method is for saved recipes.
    @Transaction
    @Query("SELECT * FROM RecipeLocal WHERE isTemp = :isTemp")
    fun getRecipesAndIngredients(isTemp: Boolean): List<LocalRecipeAndIngredientsRelation>

}