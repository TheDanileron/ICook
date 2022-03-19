package com.project.icook.ui.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.icook.AppLogger
import com.project.icook.ConnectionHelper
import com.project.icook.OnNetworkAvailabilityListener
import com.project.icook.model.data.Recipe
import com.project.icook.model.data.RecipeDataSourceState
import com.project.icook.model.repositories.IngredientsRepository
import com.project.icook.model.repositories.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeListViewModel(private val recipeRepository: RecipeRepository,private val ingredientsRepository: IngredientsRepository, application: Application): AndroidViewModel(application), OnNetworkAvailabilityListener {
    val TAG = "RecipeViewModel"
    val recipeList: MutableLiveData<List<Recipe>> = MutableLiveData(mutableListOf())
    var sorting = Sorting.BY_TIME
    var isSavedList: Boolean = false

    fun getRandomRecipeList() {
        if(isSavedList)
            return
        viewModelScope.launch(Dispatchers.IO) {
            if(ConnectionHelper.isNetworkAvailable) {
                recipeRepository.getRandomRecipes().let {result ->
                    if(result.isSuccess) {
                        AppLogger.i(TAG, "Success, data: ${result.getOrDefault(mutableListOf())}")

                        result.getOrNull()?.let {
                            updateTempList(it)
                            onListReceived(it)
                        }
                    } else {
                        AppLogger.i(TAG, result.toString())
                    }
                }
            } else {
                recipeRepository.getRandomRecipesLocal().let {result ->
                    if(result.isSuccess) {
                        AppLogger.i(TAG, "Success local, data: ${result.getOrDefault(mutableListOf())}")

                        result.getOrNull()?.let {
                            onListReceived(it)
                        }
                    } else {
                        AppLogger.i(TAG, result.toString())
                    }
                }
            }
        }
    }

    private suspend fun updateTempList(list: List<Recipe>) {
        deleteTempList()
        saveTempList(list)
    }

    private suspend fun saveTempList(list: List<Recipe>) {
        list.forEach {
            it.isTemp = true
            it.isSaved = true
            val id = recipeRepository.saveRecipe(it)
            ingredientsRepository.saveIngredients(it.ingredients, id.getOrDefault(0))
        }
    }

    private suspend fun deleteTempList() {
        recipeRepository.deleteTempRecipes()
    }

    fun getSavedRecipesList() {
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.getLocalRecipes(false).let {result ->
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
        this.isSavedList = isLocal
        if(isLocal && recipeList.value!!.isEmpty())  {
            getSavedRecipesList()
        } else{
            ConnectionHelper.subscribe(this)
            if(recipeList.value!!.isEmpty()) {
                getRandomRecipeList()
            }
        }
    }

    override fun onCleared() {
        ConnectionHelper.unsubscribe(this)
        super.onCleared()
    }

    fun onCurrentRecipeStateChanged(state: RecipeDataSourceState) {
        when(state){
            RecipeDataSourceState.IDLE -> {
                if (isSavedList) {
                    getSavedRecipesList()
                }
            }
            else -> {}
        }
    }

    private fun onListReceived(list: List<Recipe>) {
        if(isSavedList) {
            recipeList.postValue( list.map {
                it.isSaved = true
                it
            })
        }
        recipeList.postValue(list)
    }

    override fun onAvailable() {
        if(!isSavedList && recipeList.value!!.isEmpty()) {
            getRandomRecipeList()
        }
    }

    override fun onLost() {

    }

    fun sortingSwitched(sorting: Sorting) {
        if(this.sorting != sorting){
            this.sorting = sorting
            val list = recipeList.value

            when(sorting) {
                Sorting.BY_TIME -> {
                    recipeList.value = list?.sortedBy { it.readyInMinutes }
                }
                Sorting.BY_TITLE -> {
                    recipeList.value = list?.sortedBy { it.title }
                }
            }
        }
    }


    enum class Sorting{
        BY_TIME, BY_TITLE
    }

}