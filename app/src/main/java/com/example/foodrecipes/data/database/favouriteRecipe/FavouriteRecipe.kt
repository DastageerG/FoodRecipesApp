package com.example.foodrecipes.data.database.favouriteRecipe

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodrecipes.data.model.RecipeResult

@Entity(tableName = "favouriteRecipes")
class FavouriteRecipe
(
    @PrimaryKey(autoGenerate = true)
    var id:Int ,
    var recipeResult: RecipeResult
)
{

}