package com.project.icook.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.project.icook.model.data.Recipe
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recipe_preview_item.view.*

class RecipeViewHolder(val v: View): RecyclerView.ViewHolder(v) {
   fun bind(recipe: Recipe) {
      v.title.text = recipe.title
      Picasso.get().load(recipe.imageUrl).into(v.image)
   }
}