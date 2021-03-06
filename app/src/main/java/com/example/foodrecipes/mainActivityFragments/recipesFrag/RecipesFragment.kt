package com.example.foodrecipes.mainActivityFragments.recipesFrag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.foodrecipes.databinding.FragmentRecipesBinding


class RecipesFragment : Fragment()
{
    lateinit var binding :FragmentRecipesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentRecipesBinding.inflate(inflater,container,false)

        binding.recyclerViewRecipesFragment.showShimmer()

        return binding.root
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
       binding == null
    }


}