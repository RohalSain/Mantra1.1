package com.developer.rohal.mantra

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View


/**
 * Created by rohal on 28/3/18.
 */
class Loader
{
    var CustomLoader:Dialog?=null
    var Dialog:Dialog?=null
    var loaderContext:Context?=null
    fun ShowLoader(context: Context)
    {
        loaderContext=context
        var builder: AlertDialog.Builder = AlertDialog.Builder(context)
        var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view: View = inflater.inflate(R.layout.fragment_loader, null)
        builder.setView(view)
        Dialog=builder.create()
        (Dialog as AlertDialog?)?.show()
    }
    fun HideLoader()
    {
        Dialog?.dismiss()
    }
    fun ShowCustomLoader(context: Context)
    {
        loaderContext = context
        CustomLoader = Dialog(context)
        CustomLoader!!.setContentView(R.layout.fragment_loader)
        CustomLoader!!.getWindow().setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
        CustomLoader!!.window.attributes.windowAnimations = R.style.customAnimationsgrow
        CustomLoader!!.show()
    }
    fun HideCustomLoader()
    {
        CustomLoader?.dismiss()
    }
    fun ComingSoon(context: Context)
    {
        loaderContext=context
        CustomLoader = Dialog(context)

        CustomLoader!!.setContentView(R.layout.comingsoon)
        CustomLoader!!.getWindow().setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
        CustomLoader!!.window.attributes.windowAnimations = R.style.customAnimationsgrow
        CustomLoader!!.show()
    }

}