package com.project.icook.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.project.icook.R
import com.project.icook.RecipeApplication
import com.project.icook.ui.view_models.RecipeListViewModel
import com.project.icook.ui.view_models.RecipeViewModelFactory

class RecipeListFragment(): Fragment(){
    val viewModel: RecipeListViewModel
        get() = ViewModelProvider(this, RecipeViewModelFactory((requireContext().applicationContext as RecipeApplication).recipeRepository)).get(RecipeListViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipe_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.recipeList.observe(viewLifecycleOwner, Observer {
            Log.d("Test", "recipeList update: $it")
        })
        viewModel.getRandomRecipeList()
    }
}