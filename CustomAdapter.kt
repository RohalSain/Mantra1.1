package com.developer.rohal.mantra
import android.annotation.TargetApi

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.items.view.*
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.*
import android.support.v4.app.FragmentActivity
import android.content.Intent
import android.net.Uri
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList




class CustomAdpater(var userlist: ArrayList<PojoAllQuoteDetailDashBoard>, var context: Context,val listener:CustomAdpater.OnViewClicked): RecyclerView.Adapter<CustomAdpater.ViewHolder>() {
    var loader = Loader()
    var use = ReuseMethod()
    var i = 0;

    @TargetApi(Build.VERSION_CODES.M)
    interface OnViewClicked {
        fun onClick(position: Int)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.Reflection?.setOnClickListener {
            var obj = LoginDataUser.instance
            var item: PojoAllQuoteDetailDashBoard = userlist.get(position)
            obj.Quotes = item.text
            obj.BackGround = item.background
            obj.QuoteID = item.id
            Log.d("String", "${obj.Quotes} ${obj.BackGround}");
            val transaction = (context as FragmentActivity).supportFragmentManager.beginTransaction()
            //transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            var fragmentLogin = fragmentRecyclerViewReflection()
            transaction?.replace(R.id.container, fragmentLogin)
            transaction?.addToBackStack("Dashboard Page")
            transaction?.commit()

        }
        holder?.favourite?.setOnClickListener {
            var item: PojoAllQuoteDetailDashBoard = userlist.get(position)
            Log.d("String", "${item.text} ${item.id} ${item.like}")
            // Log.d("String", "${item.text} ${item.id} ${item.like}")
            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(BasicAuthInterceptor("mantrame@emilence.com", "Emilence@1"))
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build()
            val retrofit = Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://139.59.18.239:6010/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val redditAP = retrofit.create(RedditAPI::class.java)
            var obi = LoginDataUser.instance
            var call = redditAP.GetMark(obi.token!!, item.id)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                    Log.d("server", "onResponse: Server Response: " + response.toString());

                    try {
                        var json: String? = null
                        json = response?.body()?.string()
                        if (json == null) {
                            use.showSnakBar(it, "Error Occured")
                            Log.d("String", "yes")
                        } else {
                            Log.d("fav", "onResponse: json: " + json);
                            var data: JSONObject? = null;
                            data = JSONObject(json);
                            Log.d("Favourite ", "${data}")
                            var message = data.getString("message")
                            Log.d("Status ", "${message}")
                            if (message.equals("Favorite removed successfully")) {
                                Log.d("favourite remove", "yes")
                                var imgResource = R.drawable.ic_favorite_grey
                                holder?.favourite.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0)
                                //use.showSnakBar(it,"Favorite removed successfully")
                            } else {
                                Log.d("favourite add", "yes")
                                var imgResource = R.drawable.ic_selected_favorite_white
                                holder?.favourite.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0)
                                //use.showSnakBar(it,"Favourite ")
                            }
                        }


                    } catch (e: JSONException) {
                        Log.e("JSONException", "onResponse: JSONException: ");
                        Log.e("JSONException", "onResponse: JSONException: ")
                        //use.showSnakBar(it,"JSON Exception ")
                    } catch (e: IOException) {
                        Log.e("IOexception", "onResponse: JSONException: ");
                        Log.e("IOexception", "onResponse: JSONException: ")
                        loader.HideCustomLoader()
                        //use.showSnakBar(it,"JSON Exception ")
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("OnFailure", "onFailure: Something went wrong: ")
                    //Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    loader.HideCustomLoader()
                    use.showSnakBar(it, "Something went wrong ")
                }
            })

        }
        holder?.setting?.setOnClickListener {
            Log.d("Data ","yes")
            listener.onClick(position)
            /*
            val popup = PopupMenu(it.context, it)
            popup.inflate(R.menu.options)
            if(Build.VERSION.SDK_INT>22)
            {
                Log.d("Version","${Build.VERSION.SDK_INT}")
                popup.gravity=Gravity.END
            }
            popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    when (item.itemId) {
                        R.id.uploadImage -> {

                            /*
                            var obj=LoginDataUser.instance
                            var item:PojoAllQuoteDetail=userlist.get(position)
                            obj.Quotes=item.text
                            obj.BackGround=item.background
                            Log.d("String", "${obj.Quotes} ${obj.BackGround}");
                            val transaction = (context as FragmentActivity).supportFragmentManager.beginTransaction()
                            //transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                            var fragmentLogin = fragmentMakeImage()
                            transaction?.replace(R.id.container, fragmentLogin)
                            transaction?.addToBackStack("Dashboard Page")
                            transaction?.commit()
                            */
                        }
                    }//handle menu1 click
                    //handle menu2 click
                    //handle menu3 clicktoolbar.inflateMenu(R.menu.card_toolbar
                    return false
                }
            })
            //displaying the popup
            popup.show() */

        }
        holder?.share?.setOnClickListener {
            shareIt()
        }

    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.items, parent, false)
        var titleFont = Typeface.createFromAsset(parent?.context?.assets, "fonts/BubblegumSans-Regular.ttf")
        v.Share.setTypeface(titleFont)
        v.Reflection.setTypeface(titleFont)
        v.favourite.setTypeface(titleFont)
        var item:PojoAllQuoteDetailDashBoard?=null
        if(LoginDataUser.instance.chnageBackgroundPos!=null)
        {
                item = userlist.get(LoginDataUser.instance.chnageBackgroundPos!!)
                LoginDataUser.instance.chnageBackgroundPos=null
        }
        else {
            item = userlist.get(i)
        }
        Log.d("Data", "${userlist.size}")
        Log.d("Count", i.toString())
        Log.d("View", viewType.toString())
        if (item?.like == "1") {
            Log.d("like", "yes")
            val imgResource = R.drawable.ic_selected_favorite_white
            v.favourite.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0)
        }
        //v.text.setText(Html.fromHtml("Accept your past without regret. <br> Handle your present with confidence. <br> Face your future without fear.").toString())
        v.text.setText(Html.fromHtml("${item?.text}"))
        v.dashBoardRecyclerViewBackground.setImageURI(Uri.parse("http://139.59.18.239:6010/mantrame/${item?.background}?dim=500x500"))
        i++
        //v.text.setText("${}")
        v.btnDate.setText("${item?.Date}")
        v.btnDashime.setText("${item?.time}")
        return ViewHolder(v)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /*
          //var quotePic: ImageView = itemView.imageView
          var setting=itemView.setting
          var favourite=itemView.favourite
          var Reflection=itemView.Reflection
          var share=itemView.Share
           */
        var setting = itemView.setting
        var favourite = itemView.favourite
        var Reflection = itemView.Reflection
        var share = itemView.Share
        var text = itemView.text.text
    }

    private fun shareIt() {
        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.setType("text/plain");
        val shareBody = "Here is the share content body"
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    fun refreshEvents(listOfQuote: ArrayList<PojoAllQuoteDetailDashBoard>) {
        userlist = listOfQuote
        notifyDataSetChanged();
    }

    fun ChangeItem(position: Int, background: String) {
        var id = position
        var item: PojoAllQuoteDetailDashBoard = userlist.get(position)
        //change background
        Log.d("back","updated")
        userlist.set(id, PojoAllQuoteDetailDashBoard("${item.id}", "${item.text}","${background}","${item.like}","${item.Date}","${item.time}"))
        notifyItemChanged(position)
    }
}

