package com.project.icook.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.icook.R
import com.project.icook.model.data.Recipe
import com.project.icook.ui.view_models.SharedViewModel

class RecipeListRecyclerAdapter(val viewModel: SharedViewModel, val callback:() -> Unit): RecyclerView.Adapter<RecipeViewHolder>() {
    val recipes = mutableListOf<Recipe>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recipe_preview_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position])
        holder.v.setOnClickListener {
            viewModel.currentRecipe.value = recipes[position]
            callback()
        }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun updateList(list: List<Recipe>) {
        recipes.clear()
        recipes.addAll(list)
        notifyDataSetChanged()
    }
}