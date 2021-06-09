package com.example.foodrecipes.data.model.foodjoke


import com.google.gson.annotations.SerializedName

data class FoodJoke(
    @SerializedName("text")
    val text: String
)