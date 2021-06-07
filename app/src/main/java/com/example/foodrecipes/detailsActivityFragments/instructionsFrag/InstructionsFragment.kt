package com.example.foodrecipes.detailsActivityFragments.instructionsFrag

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.foodrecipes.R
import com.example.foodrecipes.data.model.RecipeResult
import com.example.foodrecipes.databinding.FragmentIngredientBinding
import com.example.foodrecipes.databinding.FragmentInstructionsBinding
import com.example.foodrecipes.utils.Constants.TAG


class InstructionsFragment : Fragment()
{
    lateinit var binding: FragmentInstructionsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentInstructionsBinding.inflate(inflater,container,false)

        val args = arguments
        val myBundle: RecipeResult? = args?.getParcelable("recipeBundle")

        binding.webViewInstructionsFrag.apply ()
        {
            webViewClient = WebViewClient()
            val url:String = myBundle!!.sourceUrl
            Log.d(TAG, "onCreateView: "+url)
            loadUrl(url)
        }

        return binding.root
    }



} // InstructionFragment closed