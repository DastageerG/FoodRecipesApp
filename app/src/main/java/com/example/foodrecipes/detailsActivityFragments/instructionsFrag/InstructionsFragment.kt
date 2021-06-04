package com.example.foodrecipes.detailsActivityFragments.instructionsFrag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodrecipes.R
import com.example.foodrecipes.databinding.FragmentIngredientBinding
import com.example.foodrecipes.databinding.FragmentInstructionsBinding


class InstructionsFragment : Fragment()
{
    lateinit var binding: FragmentInstructionsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentInstructionsBinding.inflate(inflater,container,false)

        return binding.root
    }



} // InstructionFragment closed