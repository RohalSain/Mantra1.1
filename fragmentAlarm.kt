package com.developer.rohal.mantra


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_fragment_alarm.*
import android.graphics.Color.parseColor
import android.widget.Toast
import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_POSITIVE
import android.icu.util.Calendar
import android.util.Log
import com.developer.rohal.mantra.R.styleable.AlertDialog
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker
import kotlinx.android.synthetic.main.fragment_fragment_sign_up.*
import java.util.*
import android.app.AlarmManager
import android.content.Context.ALARM_SERVICE
import android.app.PendingIntent
import android.content.Intent
import android.widget.EditText




/**
 * A simple [Fragment] subclass.
 */
class fragmentAlarm : Fragment() {


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ok.setOnClickListener {
            startAlert(it)
        }
    }

    fun startAlert(view: View) {
       var i=10;
        Log.d("Alarm","Yes yes")
        val intent = Intent(view.context, MyBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
                this.context?.applicationContext, 234324243, intent, 0)
        val alarmManager = context?.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + i * 1000, (i * 1000).toLong(), pendingIntent)
        /*alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
    + (i * 1000), 8000, pendingIntent);*/
        Toast.makeText(view.context, "Starting alarm in $i seconds",
                Toast.LENGTH_LONG).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_fragment_alarm, container, false)
    }
}