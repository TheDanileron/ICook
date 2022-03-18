package com.project.icook.ui.view_models

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.icook.model.repositories.IngredientsRepository
import com.project.icook.model.repositories.RecipeRepository

class RecipeViewModelFactory(val repository:RecipeRepository, val ingredientsRepository: IngredientsRepository,val app: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RecipeListViewModel::class.java)){
            return RecipeListViewModel(repository, app) as T
        } else if(modelClass.isAssignableFrom(SharedViewModel::class.java)){
            return SharedViewModel(repository,ingredientsRepository, app) as T
        }else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}