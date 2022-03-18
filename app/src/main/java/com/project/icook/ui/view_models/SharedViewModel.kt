package com.project.icook.ui.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.icook.R
import com.project.icook.model.data.Recipe
import com.project.icook.model.data.RecipeDataSourceState
import com.project.icook.model.data.RecipeMapper
import com.project.icook.model.repositories.IngredientsRepository
import com.project.icook.model.repositories.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel(val recipeRepository: RecipeRepository, val ingredientsRepository: IngredientsRepository, application: Application): AndroidViewModel(application)  {
    val currentRecipe = MutableLiveData<Recipe>()
    val state = MutableLiveData<RecipeDataSourceState>()
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
            return if(isCurrentRecipeSaved) getApplication<Application>().getString(R.string.menu_title_delete) else getApplication<Application>().getString(R.string.menu_title_save)
        }
    val instructionsString: String
        get() {
            return currentRecipe.value?.instructions ?: ""
        }
    val ingredientsString: String
        get() {
            return currentRecipe.value?.ingredients?.joinToString("\n") ?: ""
        }

    fun onRecipeDataSourceOperationComplete() {
        state.postValue(RecipeDataSourceState.IDLE)
    }

    fun saveMenuItemPressed() {
        if(!isCurrentRecipeSaved) {
            viewModelScope.launch(Dispatchers.IO) {
                state.postValue(RecipeDataSourceState.SAVING)

                val id = recipeRepository.saveRecipe(currentRecipe.value!!).getOrNull()

                if(id != null) {
                    currentRecipe.value?.isSaved = true
                    ingredientsRepository.saveIngredients(RecipeMapper.map(
                        currentRecipe.value!!.ingredients, id))
                }

                onRecipeDataSourceOperationComplete()
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                state.postValue(RecipeDataSourceState.DELETING)

                val rowCount = recipeRepository.deleteRecipe(currentRecipe.value!!).getOrNull()

                if(rowCount != null && rowCount > 0) {
                    currentRecipe.value?.isSaved = false
                    ingredientsRepository.deleteIngredients(RecipeMapper.map(currentRecipe.value!!.ingredients, -1))
                }

                onRecipeDataSourceOperationComplete()
            }
        }
    }
}