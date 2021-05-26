package com.example.foodrecipes.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodrecipes.data.model.FoodRecipe
import com.example.foodrecipes.data.repository.Repository
import com.example.foodrecipes.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository,application: Application) : AndroidViewModel(application)
{

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