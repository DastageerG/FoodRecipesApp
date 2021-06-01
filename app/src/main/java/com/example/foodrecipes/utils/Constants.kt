package com.example.foodrecipes.utils

object Constants
{
        const val TAG = "1234"
        const val API_KEY = "571a74dd450e4c07854bc3441ff0bd95"
        const val BASE_URL = "https://api.spoonacular.com/"

        // database constants
        const val DATABASE_NAME = "recipes_database"
        const val RECIPES_TABLE = "recipes_table"

        // queries
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        // BottomSheet Prefs
        const val DEFAULT_RECIPES_NUMBER  = "50"
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "gluten free "

        const val DATA_STORE_PREFS_NAME = "recipesPrefs"
        const val PREF_KEY_MEAL_TYPE = "mealType"
        const val PREF_KEY_MEAL_TYPE_ID = "mealTypeId"
        const val PREF_KEY_DIET_TYPE = "dietType"
        const val PREF_KEY_DIET_TYPE_ID = "dietTypeId"

        // recipes viewModel
        const val BACK_ONLINE  = "backOnline"

} // Constants