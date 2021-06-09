package com.example.foodrecipes.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.util.Log
import androidx.lifecycle.*
import com.example.foodrecipes.data.database.favouriteRecipe.FavouriteRecipe
import com.example.foodrecipes.data.database.offlineCaching.RecipeEntity
import com.example.foodrecipes.data.model.foodjoke.FoodJoke
import com.example.foodrecipes.data.model.FoodRecipe
import com.example.foodrecipes.data.repository.Repository
import com.example.foodrecipes.utils.Constants.TAG
import com.example.foodrecipes.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository,application: Application) : AndroidViewModel(application)
{


   /** ROOM DATABASE */

    /// offline caching
    val readRecipes:LiveData<List<RecipeEntity>> = repository.local.readDatabase().asLiveData()

    private fun insertRecipes(recipeEntity: RecipeEntity)
    {
        viewModelScope.launch (Dispatchers.IO)
        {
            repository.local.insertRecipes(recipeEntity)
        }
    } // insertRecipes closed

    /// favourite recipes scheme

    val readFavouriteRecipes:LiveData<List<FavouriteRecipe>> = repository.local.readFavouriteRecipes().asLiveData()

    fun insertFavouriteRecipes(favouriteRecipe: FavouriteRecipe)
    {
        viewModelScope.launch (Dispatchers.IO)
        {
            repository.local.insertFavouriteRecipes(favouriteRecipe)
        }
    } // insertRecipes closed

     fun deleteFavouriteRecipe(favouriteRecipe: FavouriteRecipe)
    {
        viewModelScope.launch (Dispatchers.IO)
        {
            repository.local.deleteFavouriteRecipes(favouriteRecipe)
        }
    } // insertRecipes closed

    fun deleteAllFavouriteRecipes() = viewModelScope.launch (Dispatchers.IO)
    {
        repository.local.deleteAllFavouriteRecipes()
    }



    /** Retrofit*/
    var recipesResponse : MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var searchRecipesResponse : MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    fun getRecipes(queryMap: HashMap<String,String>) = viewModelScope.launch ()
    {
        getRecipesSafeCall(queryMap)
    } // getRecipes closed

    private suspend fun getRecipesSafeCall(queryHashMap:HashMap<String, String>)
    {
        recipesResponse.value = NetworkResult.Loading()
        if(hasInternetConnection())
        {
            try
            {
                val response = repository.remote.getRecipes(queryHashMap)
                recipesResponse.value = handleFoodRecipesResponse(response)

                val foodRecipe = recipesResponse.value!!.data
                if(foodRecipe!=null)
                {
                    offlineCache(foodRecipe)
                }

            }catch (e:Exception)
            {
                recipesResponse.value = NetworkResult.Error("No Recipes Found.")
            } // catch closed
        } // if closed
        else
        {
            recipesResponse.value = NetworkResult.Error("No Internet Connection.")
        } // else closed
    } // getRecipesSafeCall closed


    fun searchRecipes(searchQueryMap: HashMap<String, String>)
    {
        viewModelScope.launch ()
        {
            searchRecipesSafeCall(searchQueryMap)
        }
    } // searchRecipes closed

   private suspend fun searchRecipesSafeCall(searchQueryMap: java.util.HashMap<String, String>)
    {
        searchRecipesResponse.value = NetworkResult.Loading()
        if(hasInternetConnection())
        {
            try
            {
                val response = repository.remote.searchRecipes(searchQueryMap)
                searchRecipesResponse.value = handleFoodRecipesResponse(response)
            } // try closed
            catch (e: Exception)
            {
                searchRecipesResponse.value = NetworkResult.Error(e.message)
            } // catch closed
        }
        else
        {
            searchRecipesResponse.value = NetworkResult.Error("No Internet Connection")
        }

    } // searchRecipesSafe Call closed


    private fun offlineCache(foodRecipe: FoodRecipe)
    {
        val recipeEntity = RecipeEntity(foodRecipe)
        insertRecipes(recipeEntity)
    } // offlineCache closed

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>
    {
        return when
        {
            response.message().toString().contains("timeout") ->  NetworkResult.Error("Time Out.")
            response.code() == 402                          -> NetworkResult.Error("APi Key Limited.")
            response.body()!!.recipeResults.isNullOrEmpty() -> NetworkResult.Error("Recipes Not Fount.")
            response.isSuccessful                           -> NetworkResult.Success(response.body()!!)
            else -> NetworkResult.Error(response.message().toString())
        } // return when closed
    }


    //// Food Joke method

    val foodJokeResponse:MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()

    fun getFoodJoke() = viewModelScope.launch()
    {
        getFoodJokeSafeCall();
    }

    private suspend fun getFoodJokeSafeCall()
    {
        foodJokeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection())
        {
            try
            {
                val response = repository.remote.getRandomFoodJoke()
                foodJokeResponse.value = handleFoodJokeResponse(response);


            } // try closed
            catch (e:Exception)
            {
                foodJokeResponse.value = NetworkResult.Error("Time Out")
                //Log.d(TAG, "getFoodJokeSafeCall: "+e.message)
            } // catch


        } // if closed
        else
        {
            foodJokeResponse.value = NetworkResult.Error("No Internet Connection")
        }
    } // getFoodJokeSafeCall


    private  fun handleFoodJokeResponse(response: Response<FoodJoke>): NetworkResult<FoodJoke>?
    {
       return when
       {
           response.message().toString().contains("timeout") ->  NetworkResult.Error("Time Out.")
           response.code() == 402                          -> NetworkResult.Error("APi Key Limited.")
           response.body()!!.text.isNullOrEmpty() -> NetworkResult.Error("No Food Joke Found.")
           response.isSuccessful                           -> NetworkResult.Success(response.body()!!)
           else -> NetworkResult.Error(response.message().toString())
       } //


    } // handleFoodJokeResponse closed


    // check Network Availability
    private fun hasInternetConnection() : Boolean
    {
      val connectivityManager = getApplication<Application>()
              .getSystemService(Context.CONNECTIVITY_SERVICE)
              as ConnectivityManager
      val activeNetwork = connectivityManager.activeNetwork ?: return false
      val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

      return when
      {
          capabilities.hasTransport(TRANSPORT_WIFI) -> true
          capabilities.hasTransport(TRANSPORT_ETHERNET)-> true
          capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
          else -> false
      } // return When closed
    } // hasInternetConnection





} // MainViewModel closed