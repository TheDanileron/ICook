package com.project.icook.ui.view_models

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.icook.AppLogger
import com.project.icook.ConnectionHelper
import com.project.icook.OnNetworkAvailabilityListener
import com.project.icook.R
import com.project.icook.model.data.Recipe
import com.project.icook.model.data.RecipeDataSourceState
import com.project.icook.model.data.RecipeMapper
import com.project.icook.model.repositories.IngredientsRepository
import com.project.icook.model.repositories.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel(val recipeRepository: RecipeRepository, val ingredientsRepository: IngredientsRepository, application: Application): AndroidViewModel(application), OnNetworkAvailabilityListener  {
    val TAG = "SharedViewModel"
    val currentRecipe = MutableLiveData<Recipe>()
    val dataSourceState = MutableLiveData<RecipeDataSourceState>()
    val error = MutableLiveData<String>()
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
    val nutritionInfo: String
        get() {
            return currentRecipe.value?.ingredients?.joinToString("\n") ?: ""
        }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            ConnectionHelper.onNetworkStateChanged(true)
            super.onAvailable(network)
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
        }

        override fun onLost(network: Network) {
            ConnectionHelper.onNetworkStateChanged(false)
            super.onLost(network)
        }
    }


    fun requestNetworkChanges() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        val connectivityManager = getApplication<Application>().getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }

    fun onRecipeDataSourceOperationComplete() {
        dataSourceState.postValue(RecipeDataSourceState.IDLE)
    }

    fun saveMenuItemPressed() {
        viewModelScope.launch(Dispatchers.IO) {
            if(!isCurrentRecipeSaved) {
                dataSourceState.postValue(RecipeDataSourceState.SAVING)

                val id = recipeRepository.saveRecipe(currentRecipe.value!!).getOrNull()

                if (id != null) {
                    currentRecipe.value?.isSaved = true
                    ingredientsRepository.saveIngredients(
                        RecipeMapper.map(
                            currentRecipe.value!!.ingredients, id
                        )
                    )
                }

                onRecipeDataSourceOperationComplete()
            }else {
                dataSourceState.postValue(RecipeDataSourceState.DELETING)

                recipeRepository.deleteRecipe(currentRecipe.value!!).getOrNull()
                currentRecipe.value?.isSaved = false

                onRecipeDataSourceOperationComplete()
        }
    }
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