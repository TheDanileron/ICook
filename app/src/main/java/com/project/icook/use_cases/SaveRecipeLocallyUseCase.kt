package com.project.icook.use_cases

import com.project.icook.model.repositories.RecipeRepository

class SaveRecipeLocallyUseCase {
    private val localRep: RecipeRepository? = null

    // In Kotlin, you can make use case class instances callable as functions
    // by defining the invoke() function with the operator modifier.
    operator fun invoke(): String {
        return "formatter.format(date)"
    }
}