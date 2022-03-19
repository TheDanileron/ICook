package com.project.icook.model.data

import com.project.icook.model.db.IngredientLocal
import com.project.icook.model.db.LocalRecipeAndIngredientsRelation
import com.project.icook.model.db.RecipeLocal

class RecipeMapper {
    companion object{
        fun map(localRecipe: RecipeLocal, ingr: List<IngredientLocal>): Recipe{
            return Recipe(
                localRecipe.id,
                localRecipe.title,
                localRecipe.readyInMinutes,
                localRecipe.imageUrl,
                localRecipe.summary,
                localRecipe.instructions,
                localRecipe.isVegan,
                localRecipe.isHealthy,
                true,
                localRecipe.isTemp,
                ingr.map {
                    Ingredient(it.id, it.name, it.unit, it.amount)
                }
            )
        }

        fun map(recipe: Recipe):RecipeLocal{
            return RecipeLocal(
                recipe.id,
                recipe.title,
                recipe.readyInMinutes,
                recipe.imageUrl,
                recipe.summary,
                recipe.instructions,
                recipe.isVegan,
                recipe.isHealthy,
                recipe.isTemp
            )
        }

        fun map(list: List<Ingredient>, recipeId: Long): List<IngredientLocal>{
            return list.map {
                IngredientLocal(it.id, recipeId ,it.name, it.unit, it.amount,)
            }
        }

        fun mapToRelation(recipe: Recipe): LocalRecipeAndIngredientsRelation{
            return LocalRecipeAndIngredientsRelation(
                RecipeLocal(
                    recipe.id,
                    recipe.title,
                    recipe.readyInMinutes,
                    recipe.imageUrl,
                    recipe.summary,
                    recipe.instructions,
                    recipe.isVegan,
                    recipe.isHealthy,
                    false
                ),
                recipe.ingredients.map {
                    IngredientLocal(it.id, recipe.id ,it.name, it.unit, it.amount,)
                }
            )
        }


    }
}