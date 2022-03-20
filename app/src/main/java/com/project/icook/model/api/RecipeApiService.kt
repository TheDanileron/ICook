package com.project.icook.model.api

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*


interface RecipeApiService {

    @GET("random")
    @Headers(
        "$rapidApiHostKey: $rapidApiHostValue",
        "$rapidApiKey: $rapidApiKeyValue"
    )
    fun getRandomRecipes(@Query("number") number: Int = 2) : Call<RecipePreviewListWrapper>

    @FormUrlEncoded
    @POST("visualizeNutrition")
    @Headers(
        "$rapidApiHostKey: $rapidApiHostValue",
        "$rapidApiKey: $rapidApiKeyValue"
    )
    fun visualizeRecipeNutrition(@Field("ingredientList") ingredients: String,
                                 @Field("servings")numberOfServings: Int = 1) : Call<String>

    companion object {
        const val rapidApiHostValue = "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com"
        const val rapidApiHostKey = "x-rapidapi-host"
        const val rapidApiKeyValue = "62a62d3541msh045f7f260d35af5p15e9c7jsnc9e743cc03f4"
        const val rapidApiKey = "x-rapidapi-key"
        const val BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/"

        private var retrofitService: RecipeApiService? = null

        fun getInstance() : RecipeApiService {
            if (retrofitService == null) {
                val gson = GsonBuilder()
                    .setLenient()
                    .create()
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                retrofitService = retrofit.create(RecipeApiService::class.java)
            }
            return retrofitService!!
        }
    }
}