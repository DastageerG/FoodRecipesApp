package com.example.foodrecipes.mainActivityFragments.favouriteRecipesFrag

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrecipes.R
import com.example.foodrecipes.adapter.FavouriteRecipesAdapter
import com.example.foodrecipes.adapter.RecipesAdapter
import com.example.foodrecipes.databinding.FragmentFavouriteRecipesBinding
import com.example.foodrecipes.utils.Constants.TAG
import com.example.foodrecipes.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.xml.parsers.FactoryConfigurationError


@AndroidEntryPoint
class FavouriteRecipesFragment : Fragment()
{

    private lateinit var binding:FragmentFavouriteRecipesBinding
    private lateinit var adapter:FavouriteRecipesAdapter;
    private val viewModel:MainViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentFavouriteRecipesBinding.inflate(inflater,container,false)
        adapter = FavouriteRecipesAdapter(requireActivity(),viewModel)

        setHasOptionsMenu(true)

        binding.apply()
        {
            recyclerVieFavouriteRecipes.layoutManager = LinearLayoutManager(requireContext())
            recyclerVieFavouriteRecipes.adapter = adapter
        }

        viewModel.readFavouriteRecipes.observe(viewLifecycleOwner)
        {
            Log.d(TAG, "onCreateView: "+it.size)
            if(it.size<1)
            {
                binding.imageViewNoRecipes.visibility = View.VISIBLE
                binding.textViewNoRecipes.visibility = View.VISIBLE
                binding.recyclerVieFavouriteRecipes.visibility = View.GONE
            }else
            {
                binding.imageViewNoRecipes.visibility = View.INVISIBLE
                binding.textViewNoRecipes.visibility = View.INVISIBLE
                binding.recyclerVieFavouriteRecipes.visibility = View.VISIBLE
                it?.let()
                {
                    adapter.submitList(it)
                }
            }
        }




        return binding.root
    }


    override fun onDestroy()
    {
        super.onDestroy()
        adapter.clearContextualMode()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.favourite_recipe_frag_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if(item.itemId == R.id.menu_delete_all)
        {
            Log.d(TAG, "onOptionsItemSelected: ")
            viewModel.deleteAllFavouriteRecipes()
            Snackbar.make(binding.root,"All Favourite recipes deleted",Snackbar.LENGTH_SHORT)
                    .setAction("Okay"){}
                    .show()
        } // if closed
        return super.onOptionsItemSelected(item)
    }



}