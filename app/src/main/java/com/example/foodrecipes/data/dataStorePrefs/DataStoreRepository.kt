package com.example.foodrecipes.data.dataStorePrefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.*
import androidx.datastore.preferences.core.*
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
        val selectedMealType = stringSetPreferencesKey(Constants.PREF_KEY_MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(Constants.PREF_KEY_MEAL_TYPE_ID)
        val selectedDietType = stringSetPreferencesKey(Constants.PREF_KEY_DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(Constants.PREF_KEY_DIET_TYPE_ID)
        val backOnline  = booleanPreferencesKey(Constants.BACK_ONLINE)

    } /// PrefKeys closed

   // private val dataStore: DataStore<Preferences> = context.createDataStore(name = DATA_STORE_PREFS_NAME)
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name= DATA_STORE_PREFS_NAME)

    suspend fun saveMealAndDietTypes(mealType:String, mealId:Int, dietType:String, dietId:Int)
    {
        context.dataStore.edit()
        {prefs->
            prefs[PrefKeys.selectedMealType] = setOf(mealType)
            prefs[PrefKeys.selectedMealTypeId] = mealId
            prefs[PrefKeys.selectedDietType] = setOf(dietType)
            prefs[PrefKeys.selectedDietTypeId] = dietId
        } // dataStore edit closed

    } // saveMealAndDietTypes closed

    suspend fun saveBackOnline(boolean: Boolean)
    {
        context.dataStore.edit ()
        {
          it[PrefKeys.backOnline] = boolean
        } // edit dataStore closed
    } // savBackOnline closed

    fun getBackOnlineStatus():Flow<Boolean> = context.dataStore.data
            .catch()
            {exception ->
                if(exception is IOException)
                {
                    emit(emptyPreferences())
                } // if closed
            } // catch closed
            .map ()
            {prefs ->
                val backOffline = prefs[PrefKeys.backOnline] ?:false
                backOffline
            } // map closed


    val readMealAndDietType : Flow<MealAndDietType> = context.dataStore.data
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
                MealAndDietType(selectedMealType.toString(),selectedMealTypeId, selectedDietType.toString(),selectedDietTypeId)

            }



} // DataStoreRepository

data class MealAndDietType(

    val selectedMealType:String,
    val selectedMealTypeId:Int,
    val selectedDietType:String,
    val selectedDietTypeId:Int
)