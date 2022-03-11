package com.project.icook.model.data

import androidx.room.Entity

@Entity
data class Recipe(
    val id: Int,
    val title: String,
    val readyInMinutes: Int,
    val imageUrl: String,
    val summary: String,
    val instructions: String,
    val isVegan: Boolean,
    val isHealthy: Boolean
)
