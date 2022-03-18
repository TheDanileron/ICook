package com.project.icook.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.project.icook.R
import com.project.icook.ui.fragments.RecipeDetailsFragment
import com.project.icook.ui.fragments.RecipeListFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewPager()
    }

    fun setupViewPager() {
        pager.adapter = PagerAdapter(this)
        TabLayoutMediator(tab_layout, pager) { tab, position ->
            tab.text = if(position == 0) "Browse recipes" else "Saved recipes"
        }.attach()
    }

    fun showDetailsFragment() {
        supportFragmentManager.beginTransaction().replace(fragmentContainer.id, RecipeDetailsFragment())
            .addToBackStack("").commit()
    }

    class PagerAdapter(activity: MainActivity): FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return if(position == 0) RecipeListFragment() else RecipeListFragment(true)
        }

    }
}