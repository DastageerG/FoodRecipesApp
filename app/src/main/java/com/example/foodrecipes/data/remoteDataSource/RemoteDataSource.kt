package com.example.foodrecipes.data.remoteDataSource

import com.example.foodrecipes.data.api.RecipesApi
import com.example.foodrecipes.data.model.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val api: RecipesApi)
{
    suspend fun getRecipes(queryMap:HashMap<String,String>) : Response<FoodRecipe>
    {
        return api.getRecipes(queryMap)
    } // getRecipes closed
}