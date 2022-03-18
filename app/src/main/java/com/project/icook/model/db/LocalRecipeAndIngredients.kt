package com.project.icook.model.db

import androidx.room.Embedded
import androidx.room.Relation
import com.project.icook.model.db.IngredientLocal
import com.project.icook.model.db.RecipeLocal

data class LocalRecipeAndIngredients(
    @Embedded
    val recipe: RecipeLocal,

    @Relation(parentColumn = "id", entityColumn = "recipe_id")
    val ingredients: List<IngredientLocal> = emptyList()
)