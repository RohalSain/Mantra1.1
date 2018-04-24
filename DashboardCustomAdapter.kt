package com.developer.rohal.mantra

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_dashboard_handler.view.*


class DashboardCustomAdapter(var userlist: ArrayList<PojoAllQuoteDetailDashBoard>, var context: Context,val listener:DashboardCustomAdapter.OnViewClicked): RecyclerView.Adapter<DashboardCustomAdapter.ViewHolder>() {
    var loader = Loader()
    var use = ReuseMethod()
    var i = 0;
    @TargetApi(Build.VERSION_CODES.M)
    interface OnViewClicked {
        fun onClick(position: Int)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int)
    {
        var item:PojoAllQuoteDetailDashBoard?=null
            item = userlist.get(position)

        Log.d("index ","$position")
        holder?.setting?.setOnClickListener {
            LoginDataUser.instance.clickedPos=position
            listener.onClick(position)
        }
        holder?.date?.text = item.Date
        holder?.text?.text = Html.fromHtml(item.text)
        holder?.uri?.setImageURI((Uri.parse("http://139.59.18.239:6010/mantrame/"+item.background+"?dim=500x500")),context)

    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        return ViewHolder(
                LayoutInflater.from(parent?.context).inflate(R.layout.fragment_dashboard_handler,
                        parent,
                        false ) )
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var setting = itemView.setting
        var text    = itemView.text
        var uri     = itemView.dashBoardRecyclerViewBackground
        var date    = itemView.btnDate
    }

    fun refreshEvents(listOfQuote: ArrayList<PojoAllQuoteDetailDashBoard>) {
        userlist = listOfQuote
        notifyDataSetChanged();
    }

    fun ChangeItem(position: Int, background: String) {
        Log.d("Chnages made",position.toString())
        userlist.set(position,
                PojoAllQuoteDetailDashBoard(userlist.get(position).id,
                        userlist.get(position).text,
                        background,
                        userlist.get(position).like,userlist.get(position).Date,
                        userlist.get(position).time)
        )
        notifyDataSetChanged()
        notifyItemChanged(position)
    }
}

