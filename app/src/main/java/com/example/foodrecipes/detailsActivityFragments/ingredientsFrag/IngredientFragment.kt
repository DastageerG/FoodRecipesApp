package com.example.foodrecipes.detailsActivityFragments.ingredientsFrag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodrecipes.R
import com.example.foodrecipes.databinding.FragmentIngredientBinding


class IngredientFragment : Fragment()
{
    lateinit var binding:FragmentIngredientBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentIngredientBinding.inflate(inflater,container,false)

        return binding.root
    }


} // IngredientsFragment closed