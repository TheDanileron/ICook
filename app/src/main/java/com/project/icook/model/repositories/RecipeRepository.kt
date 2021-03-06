package com.project.icook.model.repositories

import com.project.icook.model.RecipeDataSource
import com.project.icook.model.RecipeIngredientDataSource
import com.project.icook.model.api.RecipeApiService
import com.project.icook.model.data.Ingredient
import com.project.icook.model.data.Recipe
import com.project.icook.model.data.RecipeMapper
import java.lang.Exception

class RecipeRepository(var recipeService: RecipeApiService, var localDataSource: RecipeDataSource,
                       var localRecipeIngredientDataSource: RecipeIngredientDataSource) {
   suspend fun getRandomRecipes(): Result<List<Recipe>?> {
       val response = recipeService.getRandomRecipes().execute()

       if(response.isSuccessful){
            return Result.success(response.body()?.recipes)
       } else {
           return Result.failure(Exception(response.message()))
       }
   }

    suspend fun getRecipeById(id: Long): Result<Recipe> {
        val result = localRecipeIngredientDataSource.getRecipeIngredient(id).getOrNull()
        if(result != null) {
            return Result.success(RecipeMapper.map(result.recipe, result.ingredients))
        }
        return Result.failure(Exception("No such recipe"))
    }

    // each time the user loads the random list of recipes we save that list and if network goes off
    // we should load it
    suspend fun getRandomRecipesLocal(): Result<List<Recipe>?> {
        val relation = localRecipeIngredientDataSource.getRecipeIngredientList(true).getOrNull()

        return Result.success(relation?.map { RecipeMapper.map(it.recipe, it.ingredients) })
    }

    suspend fun saveRecipe(recipe: Recipe): Result<Long> {
        return localDataSource.saveRecipe(recipe)
    }

    // mark isTemp = false
    suspend fun saveTempRecipe(recipe: Recipe): Result<Int> {
        val result = recipe.copy(isTemp = false)
        return localDataSource.updateRecipe(result)
    }

    suspend fun saveRecipes(recipes: List<Recipe>): Result<Int> {
        return localDataSource.saveRecipes(recipes)
    }

    suspend fun deleteRecipe(recipe: Recipe): Result<Int> {
        return localDataSource.removeRecipe(recipe)
    }

    suspend fun deleteTempRecipes(): Result<Int> {
        return localDataSource.removeTempRecipes()
    }

    suspend fun getLocalRecipes(isTemp: Boolean) : Result<List<Recipe>?> {
        val relation = localRecipeIngredientDataSource.getRecipeIngredientList(isTemp).getOrNull()

        return Result.success(relation?.map { RecipeMapper.map(it.recipe, it.ingredients) })
    }

    suspend fun getNutrition(ingredients: List<Ingredient>): Result<String> {
        val ingredientsField = ingredients.joinToString("\n") {
            "${it.name} ${it.amount} ${it.unit}"
        }
        val response = recipeService.visualizeRecipeNutrition(ingredientsField).execute()
        if(response.isSuccessful) {
            return Result.success(response.body()!!)
        } else {
            return Result.failure(Exception("failed to load nutrition; ${response.message()}"))
        }
    }
}