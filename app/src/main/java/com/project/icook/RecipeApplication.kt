/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.project.icook

import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.project.icook.model.api.RecipeService
import com.project.icook.model.db.LocalRecipeDataSource
import com.project.icook.model.db.RecipeDB
import com.project.icook.model.repositories.RecipeRepository

/**
 * An application that lazily provides a repository. Note that this Service Locator pattern is
 * used to simplify the sample. Consider a Dependency Injection framework.
 *
 * Also, sets up Timber in the DEBUG BuildConfig. Read Timber's documentation for production setups.
 */
class RecipeApplication : Application() {

    // Depends on the flavor,
    val recipeRepository: RecipeRepository
        get() = provideRecipeRepository()

    override fun onCreate() {
        super.onCreate()
    }

    fun provideRecipeRepository(): RecipeRepository {
        return RecipeRepository(RecipeService.getInstance(),
            LocalRecipeDataSource(createDataBase(this, true).recipeDao()))
    }

    @VisibleForTesting
    fun createDataBase(
        context: Context,
        inMemory: Boolean = false
    ): RecipeDB {
        val result = if (inMemory) {
            // Use a faster in-memory database for tests
            Room.inMemoryDatabaseBuilder(context.applicationContext, RecipeDB::class.java)
                .allowMainThreadQueries()
                .build()
        } else {
            // Real database using SQLite
            Room.databaseBuilder(
                context.applicationContext,
                RecipeDB::class.java, "Tasks.db"
            ).build()
        }
        return result
    }
}
