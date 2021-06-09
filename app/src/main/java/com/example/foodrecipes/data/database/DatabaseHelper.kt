package com.example.foodrecipes.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodrecipes.data.database.favouriteRecipe.FavouriteRecipe
import com.example.foodrecipes.data.database.offlineCaching.RecipeEntity

@Database(entities = [RecipeEntity::class,FavouriteRecipe::class],exportSchema = false , version = 2)
@TypeConverters(RecipesTypeConverter::class)
abstract class DatabaseHelper : RoomDatabase()
{

    abstract fun  recipesDao(): RecipesDao

}