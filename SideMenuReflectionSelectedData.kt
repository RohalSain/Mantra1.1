package com.developer.rohal.mantra

import android.widget.ImageView

class  SideMenuReflectionSelectedData  {
    init {
        println("This ($this) is a singleton")
    }

    private object Holder {
        val INSTANCE = SideMenuReflectionSelectedData()
    }

    companion object {
        val instance: SideMenuReflectionSelectedData by lazy { Holder.INSTANCE }
    }

    var background:ImageView? = null
    var Quote:String? = null

}