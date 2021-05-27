package com.example.foodrecipes.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.ImageRequest
import com.example.foodrecipes.R
import com.example.foodrecipes.data.model.RecipeResult
import com.example.foodrecipes.databinding.LayoutRecipesItemsRowBinding

class RecipesAdapter : ListAdapter<RecipeResult,RecipesAdapter.ViewHolder>(

        object : DiffUtil.ItemCallback<RecipeResult>()
        {
            override fun areItemsTheSame(oldItem: RecipeResult, newItem: RecipeResult): Boolean
            {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: RecipeResult, newItem: RecipeResult): Boolean
            {
                return oldItem.toString() == newItem.toString()
            }
        }
)
{
    inner class  ViewHolder(view: LayoutRecipesItemsRowBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutRecipesItemsRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)

    } // onCreateViewHolder closed

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        LayoutRecipesItemsRowBinding.bind(holder.itemView).apply()
        {
            val recipe = getItem(position)

            textViewLayoutItemsRowName.text = recipe.title
            textViewLayoutItemsRowDescription.text = recipe.summary

            textViewLayoutItemsRowLikes.text = recipe.aggregateLikes.toString()
            textViewLayoutItemsRowTime.text = recipe.readyInMinutes.toString()


            imageViewRecipesItemsRow
                    .load(recipe.image)
                    {
                        crossfade(300)
                        placeholder(R.drawable.ic_place_holder_image)
                    }


            when(recipe.vegan)
            {
                true ->
                {
                    textViewLayoutItemsRowVegan.setTextColor(Color.GREEN)
                    imageViewRecipesItemsRowVegan.setColorFilter(Color.GREEN)
                }
            }

        } // binding closed


    } // onBindViewHolder closed

} // RecipesAdapter closed