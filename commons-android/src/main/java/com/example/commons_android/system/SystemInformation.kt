package com.example.commons_android.system

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class SystemInformation(private val context: Context) {
    val hasConnection get(): Boolean = with(context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager) {
        val networkInformation: NetworkInfo? = this.activeNetworkInfo

        networkInformation?.isConnectedOrConnecting ?: false
    }
}