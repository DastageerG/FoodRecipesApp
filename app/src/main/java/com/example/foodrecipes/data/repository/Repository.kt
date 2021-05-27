package com.example.foodrecipes.data.repository

import com.example.foodrecipes.data.localDataSource.LocalDataSource
import com.example.foodrecipes.data.remoteDataSource.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(remoteDataSource: RemoteDataSource,localDataSource: LocalDataSource)
{
    val remote  = remoteDataSource

    val local =  localDataSource



} /// Repository