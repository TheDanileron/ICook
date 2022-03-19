package com.project.icook

import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.platform.app.InstrumentationRegistry
import com.project.icook.model.data.RecipeMapper
import com.project.icook.model.db.IngredientsDao
import com.project.icook.model.db.RecipeDB
import com.project.icook.model.db.RecipeDao
import com.project.icook.model.db.RecipeIngredientDao
import com.project.icook.util.recipes
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test

class RecipeDBTest {
    private lateinit var database: RecipeDB
    private lateinit var recipeDao: RecipeDao
    private lateinit var ingredientDao: IngredientsDao
    private lateinit var recipeIngredientRelationDao: RecipeIngredientDao

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, RecipeDB::class.java).build()
        recipeDao = database.recipeDao()
        ingredientDao = database.ingredientsDao()
        recipeIngredientRelationDao = database.recipeIngredientDao()

    }

    @After
    fun closeDb() {
        database.close()
    }

    private fun addTestValues() {
        recipes.forEach {
            val id = recipeDao.addRecipe(RecipeMapper.map(it))

            val mappedIngredients = RecipeMapper.map(it.ingredients, id)
            ingredientDao.saveIngredients(mappedIngredients)
        }
    }

    private fun clearDB() {
        val list = recipeDao.getRecipes()

        recipeDao.removeRecipes(list)
    }

    @Test
    fun testRecipesGet() = runBlocking {
        addTestValues()
        val list = recipeDao.getRecipes()
        assertThat(list.size, equalTo(5))
    }

    @Test
    fun testRecipesDelete() = runBlocking {
        addTestValues()
        val list = recipeDao.getRecipes()

        val result = recipeDao.removeRecipes(list)
        assertThat(result, equalTo(list.size))
    }

    @Test
    fun testAddTempRecipes() = runBlocking {
        var size = 0
        recipes.forEach {
            size++
            it.isTemp = true
            val id = recipeDao.addRecipe(RecipeMapper.map(it))

            val mappedIngredients = RecipeMapper.map(it.ingredients, id)
            ingredientDao.saveIngredients(mappedIngredients)
        }

        val list = recipeIngredientRelationDao.getRecipesAndIngredients(true)
        assert(list.size == size)
        list.forEach {
            assert(it.recipe.isTemp)
        }
    }

    @Test
    fun testAddTempAndGetNonTemp() = runBlocking {
        clearDB()
        var size = 0
        recipes.forEachIndexed { index, it ->
            if(index <= 2) {
                it.isTemp = true
                recipeDao.addRecipe(RecipeMapper.map(it))
            }
        }
        recipes.forEachIndexed { index, it ->
            if(index > 2) {
                it.isTemp = false
                recipeDao.addRecipe(RecipeMapper.map(it))
            }
        }

        val list = recipeIngredientRelationDao.getRecipesAndIngredients(false)
        assert(list.isNotEmpty())
        list.forEach {
            assert(!it.recipe.isTemp)
        }
    }

    @Test
    fun testRemoveTempRecipes() = runBlocking {
        recipes.forEach {
            it.isTemp = true
            val id = recipeDao.addRecipe(RecipeMapper.map(it))

            val mappedIngredients = RecipeMapper.map(it.ingredients, id)
            ingredientDao.saveIngredients(mappedIngredients)
        }

        recipeDao.removeTempRecipes()
        val list = recipeIngredientRelationDao.getRecipesAndIngredients(true)
        assert(list.isEmpty())
    }

    @Test
    fun testMarkRecipeAsNotTemp() = runBlocking {
        clearDB()
        recipes[0].let {
            it.isTemp = true
            val id = recipeDao.addRecipe(RecipeMapper.map(it))

            it.isTemp = false
            it.id = id
            val result = recipeDao.updateRecipe(RecipeMapper.map(it))

            val list = recipeIngredientRelationDao.getRecipesAndIngredients(false)

            assert(list.size == 1)
            assert(list[0].recipe.id == id)
            assert(!list[0].recipe.isTemp)
        }
    }
}