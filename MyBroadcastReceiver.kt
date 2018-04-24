package com.developer.rohal.mantra

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import java.util.*


class MyBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        if(p1?.action.equals("com.rohal.alarmClock")) {
            var b=p1?.extras
            Toast.makeText(p0,"Alarm clicked",Toast.LENGTH_SHORT).show()
            var notify=Notifications()
            //notify.Notify(p0!!.applicationContext,"${b?.getString("meesage").toString()}",10)
            notify.Notify(p0!!.applicationContext,"Alarm has been Clicked",10)
        }
        else if(p1?.action.equals("android.intent.action.BOOT_COMPLETED")) {
            var b=p1?.extras
            var saveDate=AlarmManager(p0!!)
            saveDate.SetAlarm()
            //Toast.makeText(p0,"${b?.getString("message").toString()}",Toast.LENGTH_SHORT).show()
            Toast.makeText(p0,"Alarm clicked",Toast.LENGTH_SHORT).show()
        }
    }

}