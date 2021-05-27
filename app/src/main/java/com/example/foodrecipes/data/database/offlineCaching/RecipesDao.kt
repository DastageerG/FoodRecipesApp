package com.example.foodrecipes.data.database.offlineCaching

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao
{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipeEntity: RecipeEntity)


    @Query("Select * from recipes_table order by id asc ")
    fun readRecipes() : Flow<List<RecipeEntity>>

}