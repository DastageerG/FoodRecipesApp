package com.example.foodrecipes.data.api

import com.example.foodrecipes.data.model.foodjoke.FoodJoke
import com.example.foodrecipes.data.model.FoodRecipe
import com.example.foodrecipes.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface RecipesApi
{
    @GET("recipes/complexSearch")
    suspend fun getRecipes(@QueryMap queryMap: Map<String,String>) : Response<FoodRecipe>

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(@QueryMap searchQueryMap: Map<String,String>) : Response<FoodRecipe>


    @GET("food/jokes/random")
    suspend fun getRandomFoodJoke(@Query("apiKey") apiKey:String=API_KEY) : Response<FoodJoke>


} // RecipesApi closed