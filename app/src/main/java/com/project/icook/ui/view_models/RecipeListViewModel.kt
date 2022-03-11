package com.project.icook.ui.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.icook.model.data.RecipePreview
import com.project.icook.model.data.Result
import com.project.icook.model.repositories.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeListViewModel(private val recipeRepository: RecipeRepository): ViewModel() {
    val recipeList: MutableLiveData<List<RecipePreview>> = MutableLiveData(mutableListOf())

    fun getRandomRecipeList() {
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.getRandomRecipes().let {result ->
                if(result is Result.Success) {
                    Log.i("TEST", result.toString())
                } else if(result is Result.Error){
                    Log.i("TEST", result.toString())
                }
            }
        }
    }

    private fun onPreviewListReceived(list: List<RecipePreview>) {
        recipeList.postValue(list)
    }
}