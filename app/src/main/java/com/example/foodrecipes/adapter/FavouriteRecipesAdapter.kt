package com.example.foodrecipes.adapter

import android.graphics.Color
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.example.foodrecipes.R
import com.example.foodrecipes.data.database.favouriteRecipe.FavouriteRecipe
import com.example.foodrecipes.databinding.LayoutRecipesItemsRowBinding
import com.example.foodrecipes.mainActivityFragments.favouriteRecipesFrag.FavouriteRecipesFragmentDirections
import com.example.foodrecipes.utils.Utils
import com.example.foodrecipes.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class FavouriteRecipesAdapter(private val requireActivity:FragmentActivity,private val viewModel: MainViewModel) : ListAdapter<FavouriteRecipe,FavouriteRecipesAdapter.ViewHolder>
(

        object : DiffUtil.ItemCallback<FavouriteRecipe>()
        {
            override fun areItemsTheSame(oldItem: FavouriteRecipe, newItem: FavouriteRecipe): Boolean
            {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FavouriteRecipe, newItem: FavouriteRecipe): Boolean
            {
                return oldItem.toString() == newItem.toString()
            }
        }
) , ActionMode.Callback
{

    private var multiSelection = false
    private var selectedRecipes = arrayListOf<FavouriteRecipe>()
    private var myViewHolders = arrayListOf<ViewHolder>()
    private lateinit var rootView:View

    private lateinit var actionMode:ActionMode


    inner class  ViewHolder(view: LayoutRecipesItemsRowBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutRecipesItemsRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)

    } // onCreateViewHolder closed

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val favouriteRecipe = getItem(position)

        myViewHolders.add(holder)

        rootView  = holder.itemView.rootView

        saveItemSelectionOnScroll(holder,favouriteRecipe)

        LayoutRecipesItemsRowBinding.bind(holder.itemView).apply()
        {


            textViewLayoutItemsRowName.text = favouriteRecipe.recipeResult.sourceName
            textViewLayoutItemsRowDescription.text = Utils.parseHtmlTags(favouriteRecipe.recipeResult.summary)

            textViewLayoutItemsRowLikes.text = favouriteRecipe.recipeResult.aggregateLikes.toString()
            textViewLayoutItemsRowTime.text = favouriteRecipe.recipeResult.readyInMinutes.toString()

            Picasso.get()
                    .load(favouriteRecipe.recipeResult.image)
                    .placeholder(R.drawable.ic_place_holder_image)
                    .into(imageViewRecipesItemsRow)
            when(favouriteRecipe.recipeResult.vegan)
            {
                true ->
                {
                    textViewLayoutItemsRowVegan.setTextColor(Color.GREEN)
                    imageViewRecipesItemsRowVegan.setColorFilter(Color.GREEN)
                }
            }
        } // binding closed


        holder.itemView.setOnClickListener()
        {
            if(multiSelection)
            {
                applySelection(holder,favouriteRecipe)
            } else
            {
                val action = FavouriteRecipesFragmentDirections.actionFavouriteRecipesFragmentToDetailsActivity(favouriteRecipe.recipeResult)
                holder.itemView.findNavController().navigate(action)
            }
        } // onClickListener closed

        holder.itemView.setOnLongClickListener ()
        {

            if(!multiSelection)
            {

                requireActivity.startActionMode(this)
                applySelection(holder,favouriteRecipe)
                multiSelection = true
                true
            }
            else
            {
                applySelection(holder,favouriteRecipe)
                true
            }


        } // holder.itemView.setOnLongClickListener

    } // onBindViewHolder closed


    fun saveItemSelectionOnScroll(holder: ViewHolder,currentRecipe:FavouriteRecipe)
    {
        if(selectedRecipes.contains(currentRecipe))
        {
            changeRecipeStyle(holder,R.color.cardBackGroundLightColor,R.color.colorPrimary)
        } // if closed
        else
        {
            changeRecipeStyle(holder,R.color.cardBackGroundColor,R.color.strokeColor)
        }
    } // applySelection closed






    fun applySelection(holder: ViewHolder,currentRecipe:FavouriteRecipe)
    {
        if(selectedRecipes.contains(currentRecipe))
        {
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder,R.color.cardBackGroundColor,R.color.strokeColor)
            applyActionModeTitle()
        } // if closed
        else
        {
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder,R.color.cardBackGroundLightColor,R.color.colorPrimary)
            applyActionModeTitle()
        }
    } // applySelection closed

    private fun changeRecipeStyle(holder: ViewHolder,color: Int,strokeColor:Int)
    {
        LayoutRecipesItemsRowBinding.bind(holder.itemView).apply ()
        {
            constraintLayoutRecipesItemLayout.setBackgroundColor(ContextCompat.getColor(requireActivity,color))
            cardViewRecipesItemLayout.setStrokeColor(ContextCompat.getColor(requireActivity,strokeColor))
        } // LayoutRecipesItemsRowBinding
    } // changeRecipeStyle



    private fun applyActionModeTitle()
    {
        when(selectedRecipes.size)
        {
            0 ->
            {
                actionMode.finish()
                multiSelection = false
            }
            1-> actionMode.title = "${selectedRecipes.size} item selected"

            else ->
            {
                actionMode.title = "${selectedRecipes.size} item selected"
            }
        } // when closed
    } // actionModeTitle closed

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean
    {
        mode?.menuInflater?.inflate(R.menu.contextual_action__menu,menu)
        actionMode = mode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    } // onCreateActionMode


    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean
    {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean
    {
        if(item?.itemId == R.id.menu_delete)
        {
            selectedRecipes.forEach()
            {
                viewModel.deleteFavouriteRecipe(it)
                showSnackBarMessage("${selectedRecipes.size} recipe/s removed");
            } // forEach closed

            multiSelection = false
            selectedRecipes.clear()
            actionMode.finish()

        } // if closed
        return true
    }

    private fun showSnackBarMessage(message: String)
    {
        Snackbar.make(rootView,message,Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyActionMode(mode: ActionMode?)
    {
        applyStatusBarColor(R.color.statusBarColor)
        myViewHolders.forEach()
        {
            holder->
            changeRecipeStyle(holder,R.color.cardBackGroundColor,R.color.strokeColor)
        }

        multiSelection = false
        selectedRecipes.clear()
    }

    fun applyStatusBarColor(color:Int)
    {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity,color)
    }

    fun clearContextualMode()
    {
        if(this::actionMode.isInitialized)
        {
            actionMode.finish()
        }
    }

} // RecipesAdapter closed
