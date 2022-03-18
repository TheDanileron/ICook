package com.project.icook.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.icook.model.data.Recipe

@Database(entities = [RecipeLocal::class, IngredientLocal::class], version = 1, exportSchema = false)
abstract class RecipeDB : RoomDatabase(){
    abstract fun recipeDao(): RecipeDao
    abstract fun ingredientsDao(): IngredientsDao
    abstract fun recipeIngredientDao(): RecipeIngredientDao
}