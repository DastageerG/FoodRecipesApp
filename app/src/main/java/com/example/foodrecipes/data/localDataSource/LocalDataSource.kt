package com.example.foodrecipes.data.localDataSource

import com.example.foodrecipes.data.database.offlineCaching.RecipeEntity
import com.example.foodrecipes.data.database.RecipesDao
import com.example.foodrecipes.data.database.favouriteRecipe.FavouriteRecipe
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val recipesDao: RecipesDao)
{

    suspend fun insertRecipes(recipeEntity: RecipeEntity) = recipesDao.insertRecipe(recipeEntity)


    fun readDatabase() : Flow<List<RecipeEntity>> = recipesDao.readRecipes()


    suspend fun insertFavouriteRecipes(favouriteRecipe: FavouriteRecipe) = recipesDao.insertFavouriteRecipe(favouriteRecipe)
    fun readFavouriteRecipes() : Flow<List<FavouriteRecipe>> = recipesDao.readFavouriteRecipes()
    
    suspend fun deleteAllFavouriteRecipes() = recipesDao.deleteAllFavouriteRecipes()
    suspend fun deleteFavouriteRecipes(favouriteRecipe: FavouriteRecipe) = recipesDao.deleteFavouriteRecipes(favouriteRecipe)



}