package com.example.foodrecipes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.example.foodrecipes.adapter.PagerAdapter
import com.example.foodrecipes.databinding.ActivityDetailsBinding
import com.example.foodrecipes.detailsActivityFragments.ingredientsFrag.IngredientFragment
import com.example.foodrecipes.detailsActivityFragments.instructionsFrag.InstructionsFragment
import com.example.foodrecipes.detailsActivityFragments.overviewFrag.OverviewFragment
import com.google.android.material.snackbar.Snackbar

class DetailsActivity : AppCompatActivity()
{
    private val args by navArgs<DetailsActivityArgs>()
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailsBinding.inflate(layoutInflater)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if(item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

} // Details Activity closed