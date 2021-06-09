package com.example.foodrecipes.mainActivityFragments.recipesFrag.bottomSheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.foodrecipes.databinding.BottomSheetDailogueBinding
import com.example.foodrecipes.mainActivityFragments.recipesFrag.RecipesFragmentDirections
import com.example.foodrecipes.utils.Constants.DEFAULT_DIET_TYPE
import com.example.foodrecipes.utils.Constants.DEFAULT_MEAL_TYPE
import com.example.foodrecipes.utils.Constants.TAG
import com.example.foodrecipes.viewmodel.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.lang.Exception
import java.util.*

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class BottomSheetDialog : BottomSheetDialogFragment()
{

    lateinit var binding :BottomSheetDailogueBinding
    var mealTypeChip = DEFAULT_MEAL_TYPE
    var mealTypeChipId = 0
    var dietTypeChip = DEFAULT_DIET_TYPE
    var dietTypeChipId = 0
    lateinit var  recipesViewModel:RecipesViewModel;

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = BottomSheetDailogueBinding.inflate(inflater,container,false)


        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner)
        {
            mealTypeChip = it.selectedMealType
            dietTypeChip = it.selectedDietType
            Log.d(TAG, "onCreateView: "+mealTypeChip)
            Log.d(TAG, "onCreateView: meal type ID "+it.selectedMealTypeId)
            Log.d(TAG, "onCreateView: diet type:"+it.selectedDietTypeId)
            updateChip(it.selectedMealTypeId,binding.mealTypeChipGroup)
            updateChip(it.selectedDietTypeId,binding.dietTypeChipGroup)
        }



        binding.mealTypeChipGroup.setOnCheckedChangeListener{group,selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            mealTypeChip = chip.text.toString().toLowerCase(Locale.ROOT)
            mealTypeChipId = selectedChipId
        }

        binding.dietTypeChipGroup.setOnCheckedChangeListener{group,selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            dietTypeChip = chip.text.toString().toLowerCase(Locale.ROOT)
            dietTypeChipId = selectedChipId
        }


        binding.buttonBottomSheetApply.setOnClickListener()
        {
            recipesViewModel.saveMealAndDietTypes(mealTypeChip,mealTypeChipId,dietTypeChip,dietTypeChipId)
            Log.d(TAG, "onCreateView: clicked")
            val action = BottomSheetDialogDirections.actionBottomSheetDialogToRecipesFragment(true)
            findNavController().navigate(action)
        } // onClickListener closed



        return binding.root
    } // onCreateView closed

    private fun updateChip(chipId: Int, chipGroup: ChipGroup)
    {
        Log.d(TAG, "updateChip: "+chipId)
            if(chipId!=0)
            {
                try
                {
                    chipGroup.findViewById<Chip>(chipId).isChecked = true
                }catch (e:Exception)
                {
                    Log.d(TAG, "updateChip: "+e.message)
                }
            }

    } // update closed



} // BottomSheetDialog closed