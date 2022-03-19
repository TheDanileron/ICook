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

    @Insert(onConflict = IGNORE)
    fun addRecipes(recipe: List<RecipeLocal>)

    @Delete
    fun removeRecipe(recipe: RecipeLocal): Int

    @Delete
    fun removeRecipes(recipe: List<RecipeLocal>): Int

    @Query("DELETE FROM RecipeLocal where isTemp = 1")
    fun removeTempRecipes(): Int

    @Update
    fun updateRecipe(recipe: RecipeLocal): Int
}