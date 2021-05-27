package com.example.foodrecipes.daggerHilt

import android.content.Context
import androidx.room.Room
import com.example.foodrecipes.data.database.DatabaseHelper
import com.example.foodrecipes.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule
{

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = Room.
        databaseBuilder(context.applicationContext,DatabaseHelper::class.java,DATABASE_NAME)
            .build()

    @Singleton
    @Provides
    fun provideRecipesDao(databaseHelper: DatabaseHelper) = databaseHelper.recipesDao()



} // DatabaseModule closed