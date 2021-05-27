package com.example.foodrecipes.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodrecipes.data.database.offlineCaching.RecipeEntity
import com.example.foodrecipes.data.database.offlineCaching.RecipesDao
import com.example.foodrecipes.data.database.offlineCaching.RecipesTypeConverter

@Database(entities = [RecipeEntity::class],exportSchema = false , version = 1)
@TypeConverters(RecipesTypeConverter::class)
abstract class DatabaseHelper : RoomDatabase()
{

    abstract fun  recipesDao():RecipesDao

}