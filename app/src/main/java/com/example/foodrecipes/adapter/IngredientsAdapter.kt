package com.example.foodrecipes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.example.foodrecipes.R
import com.example.foodrecipes.data.model.ExtendedIngredient
import com.example.foodrecipes.databinding.LayoutIngredientsItemsRowBinding
import com.example.foodrecipes.utils.Constants.BASE_IMG_URL
import com.squareup.picasso.Picasso
import java.util.*

class IngredientsAdapter : ListAdapter<ExtendedIngredient,IngredientsAdapter.ViewHolder>
(

        object : DiffUtil.ItemCallback<ExtendedIngredient>()
        {
            override fun areItemsTheSame(oldItem: ExtendedIngredient, newItem: ExtendedIngredient): Boolean
            {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ExtendedIngredient, newItem: ExtendedIngredient): Boolean
            {
                return oldItem.toString() == newItem.toString()
            }
        }
)
{




    inner class  ViewHolder(view: LayoutIngredientsItemsRowBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutIngredientsItemsRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)

    } // onCreateViewHolder closed

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {

        LayoutIngredientsItemsRowBinding.bind(holder.itemView).apply()
        {

            val ingredient = getItem(position)

            textViewIngredientsRowLayoutName.text = ingredient.name.capitalize(Locale.ROOT)
            textViewIngredientsRowLayoutConsistency.text = ingredient.consistency
            textViewIngredientsRowLayoutAmount.text = ingredient.amount.toString()
            textViewIngredientsRowLayoutUnit.text = ingredient.unit

//            imageViewIngredientsItemsRowIngredientImg.load(BASE_IMG_URL+ingredient.image)
//                    {
//                        crossfade(300)
//                        placeholder(R.drawable.ic_place_holder_image)
//                    }

            Picasso.get()
                    .load(BASE_IMG_URL+ingredient.image)
                    .placeholder(R.drawable.ic_place_holder_image)
                    .into(imageViewIngredientsItemsRowIngredientImg)




        } // binding closed




    } // onBindViewHolder closed










} // IngredientsAdapter closed