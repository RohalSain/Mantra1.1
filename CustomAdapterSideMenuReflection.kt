package com.developer.rohal.mantra

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.items_side_menu_reflection.view.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.IOException
import kotlin.collections.ArrayList

class CustomAdpaterSideMenuReflection(var userlist: ArrayList<PojoAllQuoteDetailReflection>, var context: Context, val listener: OnItemClickListener): RecyclerView.Adapter<CustomAdpaterSideMenuReflection.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val user= userlist[position]
        holder?.Quote?.text = user.text
        holder?.QuoteImage?.setImageURI("http://139.59.18.239:6010/mantrame/${user.background}?dim=500x500")
        holder?.LayoutId?.setOnClickListener {
            var intemListener:RecyclerViewClickListener
            Log.d("String","yesyes")
            var ob=SideMenuReflectionSelectedData()
            ob.Quote=user.text
            listener.onItemClick(position)
        }
        }
    override fun getItemCount(): Int {
        return userlist.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.items_side_menu_reflection, parent, false)
        var titleFont = Typeface.createFromAsset(parent?.context?.assets, "fonts/BubblegumSans-Regular.ttf")
        return ViewHolder(v)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var QuoteImage = itemView.QuoteImage
        var Quote = itemView.Quote
        var LayoutId = itemView.shadowfull
    }
    fun removeItem(position: Int) {
        userlist.removeAt(position);
        notifyItemRemoved(position);
    }
}


