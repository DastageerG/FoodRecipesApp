package com.example.foodrecipes.data.database

import androidx.room.TypeConverter
import com.example.foodrecipes.data.model.FoodRecipe
import com.example.foodrecipes.data.model.RecipeResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConverter
{

    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe):String = Gson().toJson(foodRecipe)

    @TypeConverter
    fun stringToFoodRecipe(data: String): FoodRecipe
    {
        val list = object : TypeToken<FoodRecipe>( ){}.type

        return Gson().fromJson(data,list)
    }


    @TypeConverter
    fun recipeResultToString(recipeResult: RecipeResult):String = Gson().toJson(recipeResult)

    @TypeConverter
    fun stringToRecipeResult(data: String): RecipeResult
    {
        val list = object : TypeToken<RecipeResult>( ){}.type

        return Gson().fromJson(data,list)
    }





}