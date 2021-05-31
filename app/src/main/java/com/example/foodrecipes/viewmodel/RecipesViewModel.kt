package com.example.foodrecipes.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import androidx.datastore.preferences.edit
import androidx.lifecycle.*
import com.example.foodrecipes.data.dataStorePrefs.DataStoreRepository
import com.example.foodrecipes.data.database.offlineCaching.RecipeEntity
import com.example.foodrecipes.data.model.FoodRecipe
import com.example.foodrecipes.data.repository.Repository
import com.example.foodrecipes.utils.Constants
import com.example.foodrecipes.utils.Constants.DEFAULT_DIET_TYPE
import com.example.foodrecipes.utils.Constants.DEFAULT_MEAL_TYPE
import com.example.foodrecipes.utils.Constants.DEFAULT_RECIPES_NUMBER
import com.example.foodrecipes.utils.Constants.QUERY_ADD_RECIPE_INFORMATION
import com.example.foodrecipes.utils.Constants.QUERY_API_KEY
import com.example.foodrecipes.utils.Constants.QUERY_DIET
import com.example.foodrecipes.utils.Constants.QUERY_FILL_INGREDIENTS
import com.example.foodrecipes.utils.Constants.QUERY_NUMBER
import com.example.foodrecipes.utils.Constants.QUERY_TYPE
import com.example.foodrecipes.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository)
    : AndroidViewModel(application)
{


    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    val readMealAndDietType = dataStoreRepository.readMealAndDietType

     fun saveMealAndDietTypes(mealType:String, mealId:Int, dietType:String, dietId:Int)
    {
        viewModelScope.launch (Dispatchers.IO)
        {
            dataStoreRepository.saveMealAndDietTypes(mealType,mealId,dietType,dietId)
        }
    } // saveMealAndDietTypes closed


    fun appLyQueries(): HashMap<String, String>
    {

        viewModelScope.launch ()
        {
            readMealAndDietType.collect()
            {
                mealType = it.selectedMealType
                dietType = it.selectedDietType
            }
        }

        val queries:HashMap<String,String> = HashMap()
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = Constants.API_KEY
        queries[QUERY_TYPE] = mealType
        queries[QUERY_DIET] = dietType
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        return  queries

    } // applyQueries closed


} // MainViewModel closed