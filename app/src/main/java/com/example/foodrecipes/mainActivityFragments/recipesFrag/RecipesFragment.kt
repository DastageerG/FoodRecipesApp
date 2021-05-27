package com.example.foodrecipes.mainActivityFragments.recipesFrag

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrecipes.adapter.RecipesAdapter
import com.example.foodrecipes.databinding.FragmentRecipesBinding
import com.example.foodrecipes.utils.Constants.API_KEY
import com.example.foodrecipes.utils.Constants.TAG
import com.example.foodrecipes.utils.NetworkResult
import com.example.foodrecipes.utils.observeOnce
import com.example.foodrecipes.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RecipesFragment : Fragment()
{
    lateinit var binding :FragmentRecipesBinding
    private val  mainViewModel:MainViewModel by viewModels()
    private val recipesAdapter by lazy {RecipesAdapter()}



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentRecipesBinding.inflate(inflater,container,false)

        setupRecyclerView()
        readDatabase()

        return binding.root

    } // onCreate closed

    private fun setupRecyclerView()
    {
        binding.apply()
        {
            recyclerViewRecipesFragment.adapter = recipesAdapter
            recyclerViewRecipesFragment.layoutManager = LinearLayoutManager(requireContext())
        }
        showShimmerEffect()
    } // setupRecyclerView closed

    private fun readDatabase()
    {
        lifecycleScope.launch()
        {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner)
            {

                if(it.isNotEmpty())
                {
                    Log.d(TAG, "readDatabase: read database")
                    recipesAdapter.submitList(it[0].foodRecipe.recipeResults)
                    hideShimmerEffect()
                } // if closed
                else
                {
                    requestApiData()
                } // else closed
            } // observer closed
        } // coroutine closed
    } // readDatabase closed

    private fun requestApiData()
    {
        Log.d(TAG, "requestApiData: request api")
            mainViewModel.getRecipes(appLyQueries())
            mainViewModel.recipesResponse.observe(viewLifecycleOwner)
            {
                response ->
                when(response)
                {
                   is NetworkResult.Success ->
                   {
                        hideShimmerEffect()
                        hideNoInternetViews()
                       response.data?.recipeResults?.let ()
                       {
                           recipesAdapter.submitList(it)
                       }
                   }
                    is NetworkResult.Error ->
                    {
                        hideShimmerEffect()
                        displayNoInternetViews()
                        loadFromCache()
                        Log.d(TAG, "requestApiData: "+response.message)
                    }
                    is NetworkResult.Loading ->
                    {
                        hideNoInternetViews()
                        showShimmerEffect()
                    }
                } // when closed
            }
    } // requestApiData closed

    private  fun showShimmerEffect()
    {
        binding.recyclerViewRecipesFragment.showShimmer()
    } // showShimmerEffect closed

    private fun hideShimmerEffect()
    {
        binding.recyclerViewRecipesFragment.hideShimmer()
    } // hideShimmer closed

    private fun appLyQueries(): HashMap<String, String>
    {
        val queries:HashMap<String,String> = HashMap()

        queries["number"] = "50"
        queries["apiKey"] = API_KEY
        queries["type"] = "snack"
        queries["diet"] = "vegan"
        queries["addRecipeInformation"] = "true"
        queries["fillIngredients"] = "true"
        return  queries

    } // applyQueries closed


    private fun loadFromCache()
    {
        lifecycleScope.launch()
        {
            mainViewModel.readRecipes.observe(viewLifecycleOwner)
            {
                if(it.isNotEmpty())
                {
                    recipesAdapter.submitList(it[0].foodRecipe.recipeResults)
                } // if closed
                else
                {
                    displayNoInternetViews()
                }
            } // observer closed
        } // coroutine closed
    } // readDatabase closed



    override fun onDestroyView()
    {
        super.onDestroyView()
       binding == null
    } // destroyView closed

    /** ImageView and TexView specifies There is no internet or Something is wrong are invisible by default  */
    //   make them visibile
    fun displayNoInternetViews()
    {
        binding.imageViewNoInternet.visibility = View.VISIBLE
        binding.textVieWNoInternetConnection.visibility = View.VISIBLE
    } // displayNoInternetViews

    // make them invisible
    fun hideNoInternetViews()
    {
        binding.imageViewNoInternet.visibility = View.GONE
        binding.textVieWNoInternetConnection.visibility = View.GONE
    } // hideNoInternetViews



} // RecipeFragment class closed