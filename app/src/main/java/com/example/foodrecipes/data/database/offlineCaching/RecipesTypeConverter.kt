package com.example.foodrecipes.data.database.offlineCaching

import androidx.room.TypeConverter
import com.example.foodrecipes.data.model.FoodRecipe
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConverter
{

    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe):String = Gson().toJson(foodRecipe)

    @TypeConverter
    fun stringToFoodRecipe(data: String):FoodRecipe
    {
        val list = object : TypeToken<FoodRecipe>( ){}.type

        return Gson().fromJson(data,list)
    }



}