package com.project.icook.ui.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.icook.AppLogger
import com.project.icook.ConnectionHelper
import com.project.icook.OnNetworkAvailabilityListener
import com.project.icook.R
import com.project.icook.model.data.Recipe
import com.project.icook.model.data.RecipeDataSourceState
import com.project.icook.model.repositories.IngredientsRepository
import com.project.icook.model.repositories.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel(val recipeRepository: RecipeRepository, val ingredientsRepository: IngredientsRepository, application: Application): AndroidViewModel(application), OnNetworkAvailabilityListener  {
    val TAG = "SharedViewModel"
    private val currentRecipe = MutableLiveData<Recipe>()
    val currentRecipePub: LiveData<Recipe> get() = currentRecipe
    private val dataSourceState = MutableLiveData<RecipeDataSourceState>()
    val dataSourceStatePub: LiveData<RecipeDataSourceState> get() = dataSourceState
    private val error = MutableLiveData<String>()
    val errorPub: LiveData<String> get() = error

    val recipeTitle: String
        get() {
            return currentRecipe.value?.title ?: ""
        }
    val recipeImage: String
        get() {
            return currentRecipe.value?.imageUrl ?: ""
        }
    val recipeTimeToCook: String
        get() {
            val str = getApplication<Application>().getString(R.string.minutes)
            val minutes = currentRecipe.value?.readyInMinutes ?: 0
            return "$minutes: $str"
        }
    val isVegan: String
        get() {
            val str = getApplication<Application>().getString(R.string.isVegan)
            val isVegan = currentRecipe.value?.isVegan ?: false
            return "$str $isVegan"
        }
    private val isCurrentRecipeSaved: Boolean
        get() {
            return currentRecipe.value?.isSaved ?: false
        }
    val menuItemTitle: String
        get() {
            val isTemp = currentRecipe.value?.isTemp ?: false
            return if(isCurrentRecipeSaved && !isTemp) getApplication<Application>().getString(R.string.menu_title_delete) else getApplication<Application>().getString(R.string.menu_title_save)
        }
    val instructionsString: String
        get() {
            return currentRecipe.value?.instructions ?: ""
        }
    val ingredientsString: String
        get() {
            return currentRecipe.value?.ingredients?.joinToString("\n") ?: ""
        }
    val nutritionInfo: String
        get() {
            return currentRecipe.value?.ingredients?.joinToString("\n") ?: ""
        }

    fun onRecipeDataSourceOperationComplete() {
        dataSourceState.postValue(RecipeDataSourceState.IDLE)
    }

    fun start(){
        ConnectionHelper.subscribe(this)
    }

    fun saveMenuItemPressed() {
        viewModelScope.launch(Dispatchers.IO) {
            var id: Long = -1
            if(!isCurrentRecipeSaved) {
                dataSourceState.postValue(RecipeDataSourceState.SAVING)
                id = recipeRepository.saveRecipe(currentRecipe.value!!).getOrDefault(-1)

                if (id > 0) {
                    ingredientsRepository.saveIngredients(
                        currentRecipe.value!!.ingredients, id
                    )
                }

                updateCurrentRecipe(id)
            } else if(currentRecipe.value!!.isTemp) {
                dataSourceState.postValue(RecipeDataSourceState.SAVING)
                id = currentRecipe.value!!.id
                recipeRepository.saveTempRecipe(currentRecipe.value!!)

                updateCurrentRecipe(id)
            } else {
                dataSourceState.postValue(RecipeDataSourceState.DELETING)

                recipeRepository.deleteRecipe(currentRecipe.value!!).getOrNull()

                currentRecipe.postValue(currentRecipe.value!!.copy(isSaved = false))
            }

            onRecipeDataSourceOperationComplete()
        }
    }

    fun onOpenRecipeDetails(recipe: Recipe) {
        currentRecipe.postValue(recipe)
    }

    suspend fun updateCurrentRecipe(id: Long) {
        if(id < 0)
            return
        val recipe = recipeRepository.getRecipeById(id).getOrNull()
        currentRecipe.postValue(recipe)
    }

    fun calculateNutrition() {
        viewModelScope.launch(Dispatchers.IO) {
            if(ConnectionHelper.isNetworkAvailable) {
                val result = recipeRepository.getNutrition(currentRecipe.value!!.ingredients)

                if(result.isSuccess) {
                    currentRecipe.value!!.nutritionInfoHtml = result.getOrDefault("")
                    currentRecipe.postValue(currentRecipe.value)
                    //todo show a webview fragment with nutrition visualization
                } else {
                    AppLogger.i(TAG, result.toString())
                    error.postValue("Unable to get recipe nutrition")
                }
            } else {
                error.postValue("Network not available")
            }
        }
    }

    override fun onAvailable() {

    }

    override fun onLost() {

    }

    override fun onCleared() {
        ConnectionHelper.unsubscribe(this)
        super.onCleared()
    }

}