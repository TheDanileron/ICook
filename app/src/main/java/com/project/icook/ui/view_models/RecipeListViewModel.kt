package com.project.icook.ui.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.icook.AppLogger
import com.project.icook.model.data.Recipe
import com.project.icook.model.data.RecipeDataSourceState
import com.project.icook.model.repositories.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeListViewModel(private val recipeRepository: RecipeRepository): ViewModel() {
    val TAG = "RecipeViewModel"
    val recipeList: MutableLiveData<List<Recipe>> = MutableLiveData(mutableListOf())
    var isLocal: Boolean = false

    fun getRandomRecipeList() {
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.getRandomRecipes().let {result ->
                if(result.isSuccess) {
                    AppLogger.i(TAG, "Success, data: ${result.getOrDefault(mutableListOf())}")
                    result.getOrNull()?.let { onListReceived(it) }
                } else {
                    AppLogger.i(TAG, result.toString())
                }
            }
        }
    }

    fun getSavedRecipesList() {
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.getLocalRecipes().let {result ->
                if(result.isSuccess) {
                    AppLogger.i(TAG, "Success local, data: ${result.getOrDefault(mutableListOf())}")
                    result.getOrDefault(mutableListOf())?.let { onListReceived(it) }
                } else {
                    AppLogger.i(TAG, result.toString())
                }
            }
        }
    }

    fun start(isLocal: Boolean){
        this.isLocal = isLocal
        if(isLocal && recipeList.value!!.isEmpty())  {
            getSavedRecipesList()
        } else if(recipeList.value!!.isEmpty()){
            getRandomRecipeList()
        }
    }

    fun onCurrentRecipeStateChanged(state: RecipeDataSourceState) {
        when(state){
            RecipeDataSourceState.IDLE -> {
                if (isLocal) {
                    getSavedRecipesList()
                }
            }
            else -> {}
        }
    }

    private fun onListReceived(list: List<Recipe>) {
        if(isLocal) {
            recipeList.postValue( list.map {
                it.isSaved = true
                it
            })
        }
        recipeList.postValue(list)
    }

}