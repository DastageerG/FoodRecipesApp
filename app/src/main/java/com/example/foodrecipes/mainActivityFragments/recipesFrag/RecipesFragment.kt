package com.example.foodrecipes.mainActivityFragments.recipesFrag

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrecipes.R
import com.example.foodrecipes.adapter.RecipesAdapter
import com.example.foodrecipes.databinding.FragmentRecipesBinding
import com.example.foodrecipes.utils.Constants.TAG
import com.example.foodrecipes.utils.NetworkListener
import com.example.foodrecipes.utils.NetworkResult
import com.example.foodrecipes.utils.observeOnce
import com.example.foodrecipes.viewmodel.MainViewModel
import com.example.foodrecipes.viewmodel.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.example.foodrecipes.mainActivityFragments.recipesFrag.RecipesFragmentArgs as RecipesFragmentArgs


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RecipesFragment : Fragment() , androidx.appcompat.widget.SearchView.OnQueryTextListener
{
    lateinit var binding : FragmentRecipesBinding
    private val  mainViewModel:MainViewModel by viewModels()
    private val  recipeViewModel:RecipesViewModel by viewModels()
    private val recipesAdapter  =  RecipesAdapter()
    private val args by navArgs<RecipesFragmentArgs>()
    private lateinit var networkListener: NetworkListener



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentRecipesBinding.inflate(inflater,container,false)
        setHasOptionsMenu(true)
        setupRecyclerView()


        recipeViewModel.readBackOnline.observe(viewLifecycleOwner)
        {
            recipeViewModel.backOnline = it
            Log.d(TAG, "onCreateView: "+recipeViewModel.backOnline)
        }

        networkListener = NetworkListener()
        lifecycleScope.launch()
        {
            networkListener.checkNetworkAvailAbility(requireContext()).collect()
            {status ->
                recipeViewModel.networkStatus = status
                recipeViewModel.showNetWorkStatus()
                Log.d(TAG, "onCreateView: network status "+status)
                readDatabase()
            } // status
        } /// lifeCycleScope


        // button for categories
        binding.fabCategories.setOnClickListener()
        {
            if(recipeViewModel.networkStatus)
            {
                findNavController().navigate(R.id.action_recipesFragment_to_bottomSheetDialog)
            }
            else
            {
                recipeViewModel.showNetWorkStatus()
            }
        } // clickListener closed

        recipesAdapter.setOnCLickListener ()
        {
            val action = RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(it)
            findNavController().navigate(action)
        }


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

                if(it.isNotEmpty() && !args.backFromBottomSheet)
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
            mainViewModel.getRecipes(recipeViewModel.appLyQueries())
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
                    ///    displayNoInternetViews()
                        Toast.makeText(requireContext(),"No Internet Connection",Toast.LENGTH_SHORT).show()
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

    fun searchApiData(query: String)
    {
        showShimmerEffect()

        mainViewModel.searchRecipes(recipeViewModel.applySearchQueries(query))

        mainViewModel.searchRecipesResponse.observe(viewLifecycleOwner)
        {response ->
            when(response)
            {


                is NetworkResult.Success ->
                {

                    response.data?.recipeResults?.
                    let ()
                    {
                        recipesAdapter.submitList(it)
                    }
                    hideShimmerEffect()
                } // success closed
                is NetworkResult.Error ->
                {
                    hideShimmerEffect()
                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                    loadFromCache()
                }
                is NetworkResult.Loading->
                {
                    showShimmerEffect()
                }
            } // when closed
        } // observer closed


    }// searchApi data closed




    private  fun showShimmerEffect()
    {
        binding.recyclerViewRecipesFragment.showShimmer()
    } // showShimmerEffect closed

    private fun hideShimmerEffect()
    {
        binding.recyclerViewRecipesFragment.hideShimmer()
    } // hideShimmer closed




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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.recipe_frag_search_menu,menu)

        val menuItem = menu.findItem(R.id.menuSearch)
        val searchView = menuItem.actionView  as? androidx.appcompat.widget.SearchView

        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)

    } // onCreate option menu closed

    override fun onQueryTextSubmit(query: String?): Boolean
    {
        if(query!=null)
        {
            searchApiData(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean
    {
        return true
    }


} // RecipeFragment class closed