package com.developer.rohal.mantra

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * Created by rohal on 19/4/18.
 */
class Helper
{
    var monthName = arrayOf("Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec")
    init {
        println("This ($this) is a singleton")
    }

    private object Holder {
        val INSTANCE = Helper()
    }

    companion object {
        val instance:Helper by lazy { Holder.INSTANCE }
    }
    fun shareIt(context:Context,body:String,subject:String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }
    fun pictureToFile(bitmap: Bitmap,context: Context): File {
        val f= File(context.cacheDir, "Saii.png")
        f.createNewFile()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ByteArrayOutputStream());
        FileOutputStream(f).write(ByteArrayOutputStream().toByteArray())
        FileOutputStream(f).flush();
        FileOutputStream(f).close();
        return f
    }
    fun getDate(Date:String):String
    {
        var statusDate: String? = null
        val year = StringBuffer()
        val month = StringBuffer()
        val date = StringBuffer()
        val hours = StringBuffer()
        val min = StringBuffer()

        for (j in 0..3)
            year.append(Date[j])
        for (j in 5..6)
            month.append(Date[j])
        for (j in 8..9)
            date.append(Date[j])
        for (j in 11..12)
            hours.append(Date[j])
        for (j in 14..15)
            min.append(Date[j])
        var temp = month.toString().toInt()
        statusDate = "$date ${monthName[--temp]} $year"
        var tempHours = hours.toString().toInt()
        val tempMinutes = min.toString().toInt()
        if (tempHours == 12 && tempMinutes == 0) {
            Log.d("Time", "Time is ${tempHours}:${tempMinutes} noon")
        } else if (tempHours > 12) {
            tempHours = tempHours - 12
            Log.d("Time", "Time is ${tempHours}:${tempMinutes} PM")
        } else {
            Log.d("Time", "Time is ${tempHours}:${tempMinutes} AM")
        }
        return statusDate
    }

}