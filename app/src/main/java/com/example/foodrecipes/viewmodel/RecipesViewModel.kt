package com.example.foodrecipes.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.foodrecipes.data.dataStorePrefs.DataStoreRepository
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
import com.example.foodrecipes.utils.Constants.TAG
import com.example.foodrecipes.utils.NetworkListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class RecipesViewModel
@Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository,
   )
    : AndroidViewModel(application)
{

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE


    var networkStatus = false
    var backOnline  = false

    val readMealAndDietType = dataStoreRepository.readMealAndDietType

    val readBackOnline = dataStoreRepository.getBackOnlineStatus().asLiveData()


     fun saveMealAndDietTypes(mealType:String, mealId:Int, dietType:String, dietId:Int)
    {
        viewModelScope.launch (Dispatchers.IO)
        {
            dataStoreRepository.saveMealAndDietTypes(mealType,mealId,dietType,dietId)
        }
    } // saveMealAndDietTypes closed

    fun saveBackOnline(backOnline:Boolean)
    {
        viewModelScope.launch (Dispatchers.IO)
        {
            dataStoreRepository.saveBackOnline(backOnline)
        }
    } // saveBackOnline  closed





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



    fun showNetWorkStatus()
    {
        if(!networkStatus)
        {
            Toast.makeText(getApplication(),"No Internet Connection",Toast.LENGTH_SHORT).show()

            saveBackOnline(true)
        } // if closed
        else if (networkStatus)
        {
            Log.d(TAG, "showNetWorkStatus: "+networkStatus)
            if(backOnline)
            {
                Toast.makeText(getApplication(),"We Are Back Online",Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            } // if closed
        } // else if closed
    } // networkStatus closed




} // MainViewModel closed