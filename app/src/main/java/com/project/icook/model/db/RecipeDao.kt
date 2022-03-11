package com.project.icook.model.db

import androidx.room.Dao
import androidx.room.Query
import com.project.icook.model.data.Recipe

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM Recipe")
    fun getRecipes(): List<Recipe>
}