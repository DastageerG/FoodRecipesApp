package com.example.foodrecipes.data.database.offlineCaching

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodrecipes.data.model.FoodRecipe
import com.example.foodrecipes.utils.Constants.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipeEntity
(
    var foodRecipe: FoodRecipe
)
{
    @PrimaryKey(autoGenerate = false)
    var id:Int = 0
}