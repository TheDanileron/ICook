package com.project.icook.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.project.icook.R
import com.project.icook.RecipeApplication
import com.project.icook.model.data.Recipe
import com.project.icook.model.data.RecipeDataSourceState
import com.project.icook.ui.view_models.RecipeViewModelFactory
import com.project.icook.ui.view_models.SharedViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recipe_details_fragment.*

class RecipeDetailsFragment: Fragment() {
    val sharedViewModel: SharedViewModel by lazy{
        val application = (requireContext().applicationContext as RecipeApplication)
        ViewModelProvider(requireActivity(), RecipeViewModelFactory(application.recipeRepository, application.ingredientsRepository, application)).get(
            SharedViewModel::class.java)}
    val recipeObserver = Observer<Recipe> {
        fillTheData()
    }
    val stateObserver = Observer<RecipeDataSourceState> {
        requireActivity().invalidateOptionsMenu()
    }
    val errorObserver = Observer<String> {
        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.recipe_details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().invalidateOptionsMenu()
        observeData()
    }

    fun observeData() {
        sharedViewModel.currentRecipePub.observe(requireActivity(), recipeObserver)
        sharedViewModel.dataSourceStatePub.observe(requireActivity(), stateObserver)
        sharedViewModel.errorPub.observe(requireActivity(), errorObserver)
    }

    override fun onDestroy() {
        sharedViewModel.currentRecipePub.removeObserver(recipeObserver)
        sharedViewModel.dataSourceStatePub.removeObserver(stateObserver)
        sharedViewModel.errorPub.removeObserver(errorObserver)
        super.onDestroy()
    }

    fun fillTheData(){
        title.text = sharedViewModel.recipeTitle
        timeToCook.text = sharedViewModel.recipeTimeToCook
        isVegan.text = sharedViewModel.isVegan
        if(sharedViewModel.recipeImage.isNotEmpty()) Picasso.get().load(sharedViewModel.recipeImage).into(image)
        ingredientsList.text = sharedViewModel.ingredientsString
        instructions.text = sharedViewModel.instructionsString
        btnCalculateNutrition.setOnClickListener {
            sharedViewModel.calculateNutrition()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.recipe_details_menu, menu)
        menu[0].title = sharedViewModel.menuItemTitle
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.save_menu_item)
            sharedViewModel.saveMenuItemPressed()
        return super.onOptionsItemSelected(item)
    }
}