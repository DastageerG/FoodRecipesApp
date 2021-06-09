package com.example.foodrecipes.mainActivityFragments.foodJokeFrag

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.foodrecipes.R
import com.example.foodrecipes.databinding.FragmentFoodJokeBinding
import com.example.foodrecipes.utils.Constants.TAG
import com.example.foodrecipes.utils.NetworkResult
import com.example.foodrecipes.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FoodJokeFragment : Fragment()
{

    private var _binding:FragmentFoodJokeBinding? = null
    private val binding get() = _binding!!
    private val viewModel:MainViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding =  FragmentFoodJokeBinding.inflate(inflater,container,false)

        viewModel.getFoodJoke()

        viewModel.foodJokeResponse.observe(viewLifecycleOwner)
        {
            response ->
            when(response)
            {
              is NetworkResult.Loading ->
              {
                  binding.progressBarJokeFragment.visibility = View.VISIBLE
                  binding.cardViewFoodFragment.visibility = View.GONE
              }
                is NetworkResult.Error ->
                {
                    binding.imageViewErrorJokeFragment.visibility = View.VISIBLE
                    binding.textViewErrorJokeFragment.visibility = View.VISIBLE

                    binding.progressBarJokeFragment.visibility = View.GONE
                    Log.d(TAG, "onCreateView: "+response.message)
                    binding.cardViewFoodFragment.visibility = View.GONE
                }
                is NetworkResult.Success ->
                {
                    binding.imageViewErrorJokeFragment.visibility = View.GONE
                    binding.textViewErrorJokeFragment.visibility = View.GONE
                    binding.progressBarJokeFragment.visibility = View.GONE
                    binding.cardViewFoodFragment.visibility = View.VISIBLE

                    binding.textViewFoodJoke.text = response.data?.text
                    Log.d(TAG, "onCreateView:Success "+response.data.toString())

                }

            } // when closed
        }

        return binding.root
    } //
}