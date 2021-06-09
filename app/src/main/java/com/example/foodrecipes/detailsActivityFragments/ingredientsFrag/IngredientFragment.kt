package com.example.foodrecipes.detailsActivityFragments.ingredientsFrag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrecipes.adapter.IngredientsAdapter
import com.example.foodrecipes.data.model.RecipeResult
import com.example.foodrecipes.databinding.FragmentIngredientBinding


class IngredientFragment : Fragment()
{
    lateinit var binding:FragmentIngredientBinding
    lateinit var adapter:IngredientsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentIngredientBinding.inflate(inflater,container,false)

        adapter = IngredientsAdapter()

        val args = arguments
        val myBundle: RecipeResult? = args?.getParcelable("recipeBundle")

        binding.apply ()
        {
            recyclerViewIngredientsFragment.layoutManager = LinearLayoutManager(requireContext())
            recyclerViewIngredientsFragment.adapter = adapter
            myBundle?.extendedIngredients.let()
            {
                adapter.submitList(it)
            }

        }



        return binding.root
    }


} // IngredientsFragment closed