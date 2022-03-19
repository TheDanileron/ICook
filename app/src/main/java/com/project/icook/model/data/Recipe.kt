package com.project.icook.model.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class Recipe(
    var id: Long,
    val title: String,
    val readyInMinutes: Int,
    @SerializedName("image") val imageUrl: String,
    val summary: String,
    val instructions: String,
    val isVegan: Boolean,
    val isHealthy: Boolean,
    var isSaved: Boolean = false,
    var isTemp: Boolean = false,
    @SerializedName("extendedIngredients")val ingredients: List<Ingredient> = mutableListOf()
) {
    // set at the runtime, you can use it inside webview
    var nutritionInfoHtml: String = ""
}
