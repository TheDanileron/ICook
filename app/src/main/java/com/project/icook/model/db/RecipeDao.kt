package com.project.icook.model.db

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import com.project.icook.model.data.Recipe

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM RecipeLocal")
    fun getRecipes(): List<RecipeLocal>

    @Insert(onConflict = IGNORE)
    fun addRecipe(recipe: RecipeLocal): Long

    @Delete
    fun removeRecipe(recipe: RecipeLocal): Int

    @Update
    fun updateRecipe(recipe: RecipeLocal): Int
}