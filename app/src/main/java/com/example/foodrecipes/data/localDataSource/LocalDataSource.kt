package com.example.foodrecipes.data.localDataSource

import com.example.foodrecipes.data.database.offlineCaching.RecipeEntity
import com.example.foodrecipes.data.database.offlineCaching.RecipesDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val recipesDao: RecipesDao)
{

    suspend fun insertRecipes(recipeEntity: RecipeEntity) = recipesDao.insertRecipe(recipeEntity)


    fun readDatabase() : Flow<List<RecipeEntity>> = recipesDao.readRecipes()



}