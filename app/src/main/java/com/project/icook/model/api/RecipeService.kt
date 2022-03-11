package com.project.icook.model.api

import com.project.icook.model.data.RecipePreview
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RecipeService {

    @GET("random")
    @Headers(
        "$rapidApiHostKey $rapidApiHostValue",
        "$rapidApiKey $rapidApiKeyValue"
    )
    fun getRandomRecipes(@Query("number") number: Int = 1) : Call<List<RecipePreview>>

    companion object {
        const val rapidApiHostValue = "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com"
        const val rapidApiHostKey = "x-rapidapi-host"
        const val rapidApiKeyValue = "62a62d3541msh045f7f260d35af5p15e9c7jsnc9e743cc03f4"
        const val rapidApiKey = "x-rapidapi-key"

        private var retrofitService: RecipeService? = null

        fun getInstance() : RecipeService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RecipeService::class.java)
            }
            return retrofitService!!
        }
    }
}