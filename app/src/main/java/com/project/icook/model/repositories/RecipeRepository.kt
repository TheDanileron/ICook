package com.project.icook.model.repositories

import com.project.icook.model.RecipeDataSource
import com.project.icook.model.api.RecipeService
import com.project.icook.model.data.RecipePreview
import com.project.icook.model.data.Result
import java.lang.Exception

class RecipeRepository(var recipeService: RecipeService, var localDataSource: RecipeDataSource) {
   suspend fun getRandomRecipes(): Result<List<RecipePreview>?> {
       val response = recipeService.getRandomRecipes().execute()

       if(response.isSuccessful){
            return Result.Success(response.body());
       } else {
           return Result.Error(Exception(response.message()));
       }
   }
}