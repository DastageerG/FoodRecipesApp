package com.example.foodrecipes.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.example.foodrecipes.R
import com.example.foodrecipes.data.model.RecipeResult
import com.example.foodrecipes.databinding.LayoutRecipesItemsRowBinding
import com.example.foodrecipes.utils.Utils
import com.squareup.picasso.Picasso

class RecipesAdapter : ListAdapter<RecipeResult,RecipesAdapter.ViewHolder>
(

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
        val recipe = getItem(position)
        LayoutRecipesItemsRowBinding.bind(holder.itemView).apply()
        {


            textViewLayoutItemsRowName.text = recipe.title
            textViewLayoutItemsRowDescription.text = Utils.parseHtmlTags(recipe.summary.toString())

            textViewLayoutItemsRowLikes.text = recipe.aggregateLikes.toString()
            textViewLayoutItemsRowTime.text = recipe.readyInMinutes.toString()

            Picasso.get()
                    .load(recipe.image)
                    .placeholder(R.drawable.ic_place_holder_image)
                    .into(imageViewRecipesItemsRow)

            when(recipe.vegan)
            {
                true ->
                {
                    textViewLayoutItemsRowVegan.setTextColor(Color.GREEN)
                    imageViewRecipesItemsRowVegan.setColorFilter(Color.GREEN)
                } // true
            } // when



        } // binding closed


        holder.itemView.setOnClickListener()
        {

            onItemClickListener?.let { it(recipe) }
        } //

    } // onBindViewHolder closed


    private var onItemClickListener : ((RecipeResult) -> Unit)? = null


    fun setOnCLickListener(listener: (RecipeResult)->Unit)
    {
        onItemClickListener = listener
    }








} // RecipesAdapter closed