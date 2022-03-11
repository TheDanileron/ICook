package com.project.icook.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecipePreview(
    @PrimaryKey val id: Int,
    val title: String,
    val readyInMinutes: Int,
    val imageUrl: String
    )
