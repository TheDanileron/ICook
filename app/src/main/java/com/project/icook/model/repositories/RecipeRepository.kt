package com.project.icook.model.repositories

import com.project.icook.model.RecipeDataSource
import com.project.icook.model.RecipeIngredientDataSource
import com.project.icook.model.api.RecipeApiService
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

    suspend fun saveRecipe(recipe: Recipe): Result<Long> {
        return localDataSource.saveRecipe(recipe)
    }

    suspend fun deleteRecipe(recipe: Recipe): Result<Int> {
        return localDataSource.removeRecipe(recipe)
    }

    suspend fun getLocalRecipes() : Result<List<Recipe>?> {
        val relation = localRecipeIngredientDataSource.getRecipeIngredientList().getOrNull()

        return Result.success(relation?.map { RecipeMapper.map(it.recipe, it.ingredients) })
    }


}