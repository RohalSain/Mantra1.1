package com.developer.rohal.mantra

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.items_side_menu_reflection.view.*
import kotlin.collections.ArrayList

class CustomAdapterSideMenuFaa(var userlist: ArrayList<PojoAllQuoteDetailReflection>, var context: Context, val listener: OnItemClickListener): RecyclerView.Adapter<CustomAdapterSideMenuFaa.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.Quote?.text = userlist[position].text
        holder?.QuoteImage?.setImageURI("http://139.59.18.239:6010/mantrame/${userlist[position].background}?dim=500x500")
        holder?.LayoutId?.setOnClickListener {
            SideMenuReflectionSelectedData().Quote=userlist[position].text
            listener.onItemClick(position)
        }
        }
    override fun getItemCount(): Int {
        return userlist.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.items_side_menu_fav, parent, false))
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


