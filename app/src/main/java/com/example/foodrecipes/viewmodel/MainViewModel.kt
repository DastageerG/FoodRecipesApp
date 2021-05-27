package com.example.foodrecipes.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import androidx.lifecycle.*
import com.example.foodrecipes.data.database.offlineCaching.RecipeEntity
import com.example.foodrecipes.data.model.FoodRecipe
import com.example.foodrecipes.data.repository.Repository
import com.example.foodrecipes.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository,application: Application) : AndroidViewModel(application)
{


   /** ROOM DATABASE */

    val readRecipes:LiveData<List<RecipeEntity>> = repository.local.readDatabase().asLiveData()

    private fun insertRecipes(recipeEntity: RecipeEntity)
    {
        viewModelScope.launch (Dispatchers.IO)
        {
            repository.local.insertRecipes(recipeEntity)
        }
    }

    /** Retrofit*/
    var recipesResponse : MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()



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