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
import com.example.foodrecipes.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


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
        requestApiData()

        return binding.root
    }

    private fun requestApiData()
    {
            mainViewModel.getRecipes(appLyQueries())
            mainViewModel.recipesResponse.observe(viewLifecycleOwner)
            {
                response ->
                when(response)
                {
                   is NetworkResult.Success ->
                   {
                        hideShimmerEffect()
                       response.data?.recipeResults?.let ()
                       {
                           recipesAdapter.submitList(it)
                       }
                   }
                    is NetworkResult.Error ->
                    {
                        hideShimmerEffect()
                        Log.d(TAG, "requestApiData: "+response.message)
                    }
                    is NetworkResult.Loading ->
                    {
                        showShimmerEffect()
                    }
                } // when closed
            }
    } // requestApiData closed

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

    private fun setupRecyclerView()
    {
        binding.apply()
        {
            recyclerViewRecipesFragment.adapter = recipesAdapter
            recyclerViewRecipesFragment.layoutManager = LinearLayoutManager(requireContext())
        }
        showShimmerEffect()
    } // setupRecyclerView closed

    private  fun showShimmerEffect()
    {
        binding.recyclerViewRecipesFragment.showShimmer()
    } // showShimmerEffect closed

    private fun hideShimmerEffect()
    {
        binding.recyclerViewRecipesFragment.hideShimmer()
    } // hideShimmer closed

    override fun onDestroyView()
    {
        super.onDestroyView()
       binding == null
    }


}