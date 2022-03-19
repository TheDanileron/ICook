package com.project.icook.model.db

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class RecipeLocal(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val title: String,
    val readyInMinutes: Int,
    @SerializedName("image") val imageUrl: String,
    val summary: String,
    val instructions: String,
    val isVegan: Boolean,
    val isHealthy: Boolean,
    var isTemp: Boolean
)