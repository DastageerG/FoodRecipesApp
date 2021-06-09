package com.example.foodrecipes.detailsActivityFragments.overviewFrag

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.example.foodrecipes.R
import com.example.foodrecipes.data.model.RecipeResult
import com.example.foodrecipes.databinding.FragmentOverViewBinding
import com.example.foodrecipes.utils.Utils.parseHtmlTags
import com.squareup.picasso.Picasso


class OverviewFragment : Fragment()
{
    lateinit var binding: FragmentOverViewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentOverViewBinding.inflate(inflater,container,false)

        val args = arguments
        val myBundle: RecipeResult? = args?.getParcelable("recipeBundle")

        binding.apply ()
        {

            Picasso.get()
                    .load(myBundle?.image)
                    .placeholder(R.drawable.ic_place_holder_image)
                    .into(imageViewOverViewFragmentRecipeImage)

            textViewOverViewFragmentTitle.text = myBundle?.title
            textViewOverViewFragmentDescription.text = parseHtmlTags(myBundle?.summary.toString())

            textViewOverViewFragmentLikes.text = myBundle?.aggregateLikes.toString()
            textViewOverViewFragmentTime.text = myBundle?.readyInMinutes.toString()


            if(myBundle?.vegetarian == true)
            {
                imageViewCheckVegetarian.setColorFilter(Color.GREEN)
                textViewCheckVegetarian.setTextColor(Color.GREEN)
            }


            if(myBundle?.glutenFree == true)
            {
                imageViewCheckGlutenFree.setColorFilter(Color.GREEN)
                textViewCheckGlutenFree.setTextColor(Color.GREEN)
            }


            if(myBundle?.veryHealthy == true)
            {
                imageViewCheckHealthy.setColorFilter(Color.GREEN)
                textViewCheckHealthy.setTextColor(Color.GREEN)
            }


            if(myBundle?.vegan == true)
            {
                imageViewCheckVegan.setColorFilter(Color.GREEN)
                textViewCheckVegan.setTextColor(Color.GREEN)
            }


            if(myBundle?.dairyFree == true)
            {
                imageViewCheckDiaryFree.setColorFilter(Color.GREEN)
                textViewCheckDairyFree.setTextColor(Color.GREEN)
            }


            if(myBundle?.cheap == true)
            {
                imageViewCheckCheap.setColorFilter(Color.GREEN)
                textViewCheckCheap.setTextColor(Color.GREEN)
            }


        }


        return binding.root
    } //



} // overview fragment closed