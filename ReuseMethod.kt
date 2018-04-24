package com.developer.rohal.mantra

import android.support.design.widget.Snackbar
import android.view.View

class ReuseMethod {
    companion object {
        fun showSnakBar(view: View?,message:String)
        {
            val snackBar: Snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
            snackBar.show()
        }
    }

    fun showSnakBar(view: View?,message:String)
    {
        val snackBar: Snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackBar.show()
    }

}