package com.example.foodrecipes.data.api

import com.example.foodrecipes.data.model.FoodRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RecipesApi
{
    @GET("recipes/complexSearch")
    suspend fun getRecipes(@QueryMap queryMap: Map<String,String>) : Response<FoodRecipe>
}