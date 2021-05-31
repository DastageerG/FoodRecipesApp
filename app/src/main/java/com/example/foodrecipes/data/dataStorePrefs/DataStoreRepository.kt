package com.example.foodrecipes.data.dataStorePrefs

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import com.example.foodrecipes.utils.Constants
import com.example.foodrecipes.utils.Constants.DATA_STORE_PREFS_NAME
import com.example.foodrecipes.utils.Constants.DEFAULT_DIET_TYPE
import com.example.foodrecipes.utils.Constants.DEFAULT_MEAL_TYPE
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context)
{
    private object PrefKeys
    {
        val selectedMealType = preferencesKey<String>(Constants.PREF_KEY_MEAL_TYPE)
        val selectedMealTypeId = preferencesKey<Int>(Constants.PREF_KEY_MEAL_TYPE_ID)
        val selectedDietType = preferencesKey<String>(Constants.PREF_KEY_DIET_TYPE)
        val selectedDietTypeId = preferencesKey<Int>(Constants.PREF_KEY_DIET_TYPE_ID)

    } /// PrefKeys closed

    private val dataStore: DataStore<Preferences> = context.createDataStore(name = DATA_STORE_PREFS_NAME)


    suspend fun saveMealAndDietTypes(mealType:String, mealId:Int, dietType:String, dietId:Int)
    {
        dataStore.edit()
        {prefs->
            prefs[PrefKeys.selectedMealType] = mealType
            prefs[PrefKeys.selectedMealTypeId] = mealId
            prefs[PrefKeys.selectedDietType] = dietType
            prefs[PrefKeys.selectedDietTypeId] = dietId
        } // dataStore edit closed

    } // saveMealAndDietTypes closed

    val readMealAndDietType : Flow<MealAndDietType> = dataStore.data
            .catch()
            { exception->
                if(exception is IOException)
                {
                    emit(emptyPreferences())
                }else
                {
                    throw  exception
                }
            }  // catch closed
            .map { prefs ->
                val selectedMealType = prefs[PrefKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
                val selectedMealTypeId = prefs[PrefKeys.selectedMealTypeId] ?: 0
                val selectedDietType = prefs[PrefKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
                val selectedDietTypeId = prefs[PrefKeys.selectedDietTypeId] ?: 0
                MealAndDietType(selectedMealType,selectedMealTypeId,selectedDietType,selectedDietTypeId)

            }



} // DataStoreRepository

data class MealAndDietType(

    val selectedMealType:String,
    val selectedMealTypeId:Int,
    val selectedDietType:String,
    val selectedDietTypeId:Int
)