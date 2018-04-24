package com.developer.rohal.mantra

import android.app.Application
import android.content.res.Configuration


class MyApplication : Application()
{
    var name:String? = null
    private object Holder {
        val INSTANCE = MyApplication()
    }

    companion object {
        val instance:MyApplication by lazy { Holder.INSTANCE }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onTerminate() {
        super.onTerminate()
    }


}