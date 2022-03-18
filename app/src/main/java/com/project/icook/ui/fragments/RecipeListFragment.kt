package com.project.icook.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.icook.R
import com.project.icook.RecipeApplication
import com.project.icook.model.data.Recipe
import com.project.icook.model.data.RecipeDataSourceState
import com.project.icook.ui.activities.MainActivity
import com.project.icook.ui.adapters.RecipeListRecyclerAdapter
import com.project.icook.ui.view_models.RecipeListViewModel
import com.project.icook.ui.view_models.RecipeViewModelFactory
import com.project.icook.ui.view_models.SharedViewModel
import kotlinx.android.synthetic.main.fragment_recipe_list.*

class RecipeListFragment(val isLocal: Boolean = false): Fragment(){
    val listViewModel: RecipeListViewModel by lazy{
        val application = (requireContext().applicationContext as RecipeApplication)
        ViewModelProvider(this, RecipeViewModelFactory(application.recipeRepository,application.ingredientsRepository, application)).get(RecipeListViewModel::class.java)}
    val sharedViewModel: SharedViewModel by lazy{
        val application = (requireContext().applicationContext as RecipeApplication)
        ViewModelProvider(requireActivity(), RecipeViewModelFactory(application.recipeRepository, application.ingredientsRepository, application)).get(SharedViewModel::class.java)}
    var adapter: RecipeListRecyclerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipe_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        observeRecipeList()
        setupRecycler()

        listViewModel.start(isLocal)
    }

    fun setupRecycler() {
        adapter = RecipeListRecyclerAdapter(sharedViewModel) {
            showDetails()
        }
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
    }

    fun observeRecipeList() {
        listViewModel.recipeList.observe(viewLifecycleOwner) {
            onListReceived(it)
        }

        if(isLocal) {
            sharedViewModel.state.observe(requireActivity()) {
                listViewModel.onCurrentRecipeStateChanged(it)
            }
        }
    }

    fun showDetails() {
        (activity as MainActivity).showDetailsFragment()
    }

    fun onListReceived(list: List<Recipe>) {
        adapter?.updateList(list)
        if(list.isNotEmpty())
            progress.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }
}