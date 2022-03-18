package com.project.icook.model.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.project.icook.model.data.Recipe

@Entity(foreignKeys = [
    ForeignKey(entity = RecipeLocal::class, parentColumns = ["id"], childColumns = ["recipe_id"])
],)
data class IngredientLocal(
    @PrimaryKey(autoGenerate = true) val id: Long,
    var recipe_id: Long,
    val name: String,
    val unit: String,
    val amount: Double,
)