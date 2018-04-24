package com.developer.rohal.mantra

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.iid.FirebaseInstanceId

class FirebaseInstanceId: FirebaseInstanceIdService()
{
    val REG_TOKEN="REG_TOKEN"
    var token=""
    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val i=FirebaseInstanceId.getInstance().token
        Log.d(REG_TOKEN,i)
        token= i!!

    }
}