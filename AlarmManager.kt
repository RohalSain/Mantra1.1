package com.developer.rohal.mantra

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import java.util.*

class AlarmManager {
    var context:Context?=null
    var SharedRef: SharedPreferences?=null
    constructor(context: Context)
    {
        this.context=context
        SharedRef=context.getSharedPreferences("myref",Context.MODE_PRIVATE)
    }

    fun SaveData(hour:Int,minute:Int)
    {
        var editor=SharedRef?.edit()
        editor?.putInt("hour",hour)
        editor?.putInt("minute",minute)
        editor?.commit()
    }
    fun getHour():Int {
        return SharedRef!!.getInt("hour",0)
    }
    fun getMinute():Int {
        return SharedRef!!.getInt("minute",0)
    }
    fun SetAlarm()
    {
        var hour:Int=getHour()
        var minute:Int=getMinute()
        Log.d("Shared Prefences","Time is ${hour} : ${minute}")
        // Setting Alarm Clock
        var calendor= Calendar.getInstance()
        calendor.set(Calendar.HOUR_OF_DAY,hour)
        calendor.set(Calendar.MINUTE,minute)
        calendor.set(Calendar.SECOND,0)
        val am=context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var intent= Intent(context,MyBroadcastReceiver::class.java)
        intent.putExtra("message","alarm time")
        intent.action="com.rohal.alarmClock"
        var pi= PendingIntent.getBroadcast(context,0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        am.setRepeating(AlarmManager.RTC_WAKEUP,calendor.timeInMillis, AlarmManager.INTERVAL_DAY,pi)

    }

}