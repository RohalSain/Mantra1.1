package com.developer.rohal.mantra

import android.content.Context
import android.graphics.Bitmap
import org.json.JSONObject

public class LoginDataUser private constructor() {
    init {
        println("This ($this) is a singleton")
    }

    private object Holder {
        val INSTANCE = LoginDataUser()
    }

    companion object {
        val instance:LoginDataUser by lazy { Holder.INSTANCE }
    }

    var backGroundStatus: Boolean? = null
    var backGroundChange:String?=null

    var token: String? = null
    var Quotes: String? = null
    var BackGround:String?= null
    var count:Int=0;
    var QuoteID:String=" "
    var profiePic:String=" "
    var context:Context?=null
    var FacebookGooglebitmap:Bitmap?=null
    var chnagebackground:String=" "
    var chnageBackgroundPos:Int?=null
    var content:Int?=null
    var ContextView:Context?=null
    var clickedPos:Int?=null
    var SplashScreenTimeOut = 2000
    val PickFromFile = 300
    val RequestImageCapture = 1
    var listOfQuote: ArrayList<PojoAllQuoteDetailDashBoard> = ArrayList<PojoAllQuoteDetailDashBoard>()
}