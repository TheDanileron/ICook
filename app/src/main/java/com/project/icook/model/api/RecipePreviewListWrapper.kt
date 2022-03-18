package com.project.icook.model.api

import com.project.icook.model.data.Recipe

data class RecipePreviewListWrapper(
    val recipes: List<Recipe>
)