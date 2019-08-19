package com.ouch.mvvm.helper

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by 李小璐 on 2019-08-19.
 */
@Singleton
class NetworkManager @Inject constructor(@param:Named("app") val context: Context) {
    val isConnected: Boolean
        get() {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo?.isConnected == true
        }
}