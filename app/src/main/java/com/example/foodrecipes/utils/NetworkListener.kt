package com.example.foodrecipes.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

@ExperimentalCoroutinesApi
class NetworkListener : ConnectivityManager.NetworkCallback()
{

    private val isNetworkAvailAble = MutableStateFlow<Boolean>(false)

    fun checkNetworkAvailAbility(context: Context) : MutableStateFlow<Boolean>
    {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.registerDefaultNetworkCallback(this)

        var isConnected = false

        connectivityManager.allNetworks.forEach()
        {
            val networkCapability = connectivityManager.getNetworkCapabilities(it)

            networkCapability?.let()
            {
                if(it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
                {
                    isConnected = true
                    return@forEach
                } // if closed
            } // let closed
        } // for Each closed

        isNetworkAvailAble.value = isConnected
        return isNetworkAvailAble

    } // checkNetworkAvailAbility closed

    override fun onAvailable(network: Network)
    {
        isNetworkAvailAble.value = true
    }

    override fun onLost(network: Network)
    {
        isNetworkAvailAble.value = false
    }

}