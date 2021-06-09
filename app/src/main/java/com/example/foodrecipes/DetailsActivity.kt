package com.example.foodrecipes

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.example.foodrecipes.adapter.PagerAdapter
import com.example.foodrecipes.data.database.favouriteRecipe.FavouriteRecipe
import com.example.foodrecipes.databinding.ActivityDetailsBinding
import com.example.foodrecipes.detailsActivityFragments.ingredientsFrag.IngredientFragment
import com.example.foodrecipes.detailsActivityFragments.instructionsFrag.InstructionsFragment
import com.example.foodrecipes.detailsActivityFragments.overviewFrag.OverviewFragment
import com.example.foodrecipes.utils.Constants.TAG
import com.example.foodrecipes.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity()
{
    private val args by navArgs<DetailsActivityArgs>()
    private val viewModel:MainViewModel by viewModels()
    lateinit var binding: ActivityDetailsBinding

    private var recipeSaved = false
    private var savedRecipeId = 0

    private lateinit var menuItem: MenuItem


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
         binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBarDetailsActivity)
        binding.toolBarDetailsActivity.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(OverviewFragment())
        fragmentList.add(IngredientFragment())
        fragmentList.add(InstructionsFragment())

        val titleList = ArrayList<String>()
        titleList.add("Overview")
        titleList.add("Ingredients")
        titleList.add("Instructions")



        val resultBundle = Bundle()
        resultBundle.putParcelable("recipeBundle",args.recipeDetails)

        val adapter = PagerAdapter(resultBundle,fragmentList,titleList,supportFragmentManager)

        binding.viewPagerDetailsActivity.adapter = adapter
        binding.tabLayoutDetailsActivity.setupWithViewPager(binding.viewPagerDetailsActivity)


    } // onCreate closed

    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        menuInflater.inflate(R.menu.menu_overview_frag,menu)

        menuItem = menu!!.findItem(R.id.menuFavourite)
        checkSavedRecipes(menuItem)

        return super.onCreateOptionsMenu(menu)
    }

    private fun checkSavedRecipes(item: MenuItem?)
    {
        viewModel.readFavouriteRecipes.observe(this)
        {
            favouriteRecipes ->

            for(savedRecipe in favouriteRecipes)
                if(savedRecipe.recipeResult.id == args.recipeDetails.id)
                {
                    changeMenuColor(item!!,R.color.yellow)
                    recipeSaved = true
                    savedRecipeId = savedRecipe.id
                }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if(item.itemId == android.R.id.home)
            finish()
        else if(item.itemId == R.id.menuFavourite &&  !recipeSaved)
        {
                saveToFavourites(item)
        }
        else if(item.itemId == R.id.menuFavourite &&  recipeSaved)
        {
            removeRecipefromFavourites(item)
        }


        return super.onOptionsItemSelected(item)

    } // onOptionsSelected closed

    private fun removeRecipefromFavourites(item: MenuItem)
    {
        val favouriteRecipe = FavouriteRecipe(savedRecipeId,args.recipeDetails)
        viewModel.deleteFavouriteRecipe(favouriteRecipe)
        changeMenuColor(item,R.color.white)
        showSnackBar("Recipe Removed")
    }

    private fun saveToFavourites(item: MenuItem)
    {
        val favouriteRecipe = FavouriteRecipe(0,args.recipeDetails)
        viewModel.insertFavouriteRecipes(favouriteRecipe)
        changeMenuColor(item,R.color.yellow)
        showSnackBar("Recipe Saved")
        recipeSaved = true
    }

    private fun showSnackBar(message: String)
    {
        Snackbar.make(binding.relativeLayout,message,Snackbar.LENGTH_SHORT)
                .setAction("Okay"){}
                .show()
    }


    private fun changeMenuColor(item: MenuItem,color:Int)
    {
            item.icon.setTint(ContextCompat.getColor(this,color))
    }

    override fun onDestroy()
    {
        super.onDestroy()
        changeMenuColor(menuItem,R.color.white)
    }

} // Details Activity closed