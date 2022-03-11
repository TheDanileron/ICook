package com.project.icook.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.project.icook.R
import com.project.icook.RecipeApplication
import com.project.icook.ui.fragments.RecipeListFragment
import com.project.icook.ui.view_models.RecipeListViewModel
import com.project.icook.ui.view_models.RecipeViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showListFragment()
    }

    fun showListFragment() {
        supportFragmentManager.beginTransaction().replace(fragmentContainer.id, RecipeListFragment()).commit();
    }
}