package com.developer.rohal.mantra

import android.os.Bundle
import android.support.multidex.MultiDex
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.facebook.drawee.backends.pipeline.Fresco
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(R.layout.activity_main)
        MultiDex.install(this)
        val session = Session(applicationContext)
        if (!session.loggedin()) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, FragmentSplashScreen())
            transaction.commit()
        } else if (session.loggedin()) {
            LoginDataUser.instance.token = session.getToken()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, FragmentDashboard())
            transaction.commit()
        }
        var ob=FirebaseInstanceId().token
        Log.d("token",ob)
    }
    /*

    override fun onBackPressed() {
        if(LoginDataUser.instance.content==null)
        super.onBackPressed()
        else if(LoginDataUser.instance.content==9)
        {
            var fragmentLogin = fragmentLogin()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragmentLogin)
            transaction.commit()
        }
        else if(LoginDataUser.instance.content==18)
        {
           // val builder = AlertDialog.Builder(LoginDataUser.instance.ContextView)
           // builder.setTitle("Exit")
          //  builder.setMessage("Are you sure you want to Exit?")
           // builder.setPositiveButton("YES") { dialog, which ->
          //     System.exit(1);
           // }

          //  builder.setNegativeButton("NO") { dialog, which ->
           //     dialog.dismiss()
           // }

          //  val alert = builder.create()
        //    alert.show()
            super.onBackPressed()
      }
        else
        {
            super.onBackPressed()
        }
    } */
}

