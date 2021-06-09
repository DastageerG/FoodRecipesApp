package com.example.foodrecipes.data.database

import androidx.room.*
import com.example.foodrecipes.data.database.favouriteRecipe.FavouriteRecipe
import com.example.foodrecipes.data.database.offlineCaching.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao
{

    // offline cache
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipeEntity: RecipeEntity)


    @Query("Select * from recipes_table order by id asc ")
    fun readRecipes() : Flow<List<RecipeEntity>>

    // favourite recipes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteRecipe(favouriteRecipe: FavouriteRecipe)


    @Query("Select * from favouriteRecipes order by id asc ")
    fun readFavouriteRecipes() : Flow<List<FavouriteRecipe>>


    @Delete
    suspend fun deleteFavouriteRecipes(favouriteRecipe: FavouriteRecipe)

    @Query("delete  from favouriteRecipes")
    suspend fun deleteAllFavouriteRecipes()


}