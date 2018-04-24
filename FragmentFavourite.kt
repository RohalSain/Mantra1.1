package com.developer.rohal.mantra

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_adpater_setting.view.*
import kotlinx.android.synthetic.main.fragment_fragment_favourite.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.IOException

class FragmentFavourite : Fragment() {
    var adapter: CustomAdapterSideMenuFaa?=null
    var pos: Int? = null
    var listOfQuote:ArrayList<PojoAllQuoteDetailReflection> = ArrayList<PojoAllQuoteDetailReflection>();

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(listOfQuote.size==0)
        {
            loading_indicatefav.visibility=View.VISIBLE
            apiCall((ApiCall.instance.retrofitClient()?.create(RedditAPI::class.java))?.GetAllLikeQuote(LoginDataUser.instance.token!!),"Allfav")
        }
        else
        {
            recyclerviewfavourite.layoutManager =  GridLayoutManager(context, 1)
            recyclerviewfavourite.adapter = CustomAdapterSideMenuFaa(listOfQuote, context!!, object : CustomAdapterSideMenuFaa.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    Log.d("Clicked 1 ok", "yes ${position}")
                    LoginDataUser.instance.chnageBackgroundPos = position
                    LoginDataUser.instance.QuoteID =listOfQuote.get(position).id
                    pos = position
                    customAdapterSetting(context,listOfQuote.get(position))
                }
            })
        }

               FavouriteBackButton.setOnClickListener {
            activity?.onBackPressed()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fragment_favourite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun customAdapterSetting(view: Context?, item: PojoAllQuoteDetailReflection) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val inflater: LayoutInflater = layoutInflater
        val view2: View = inflater.inflate(R.layout.fragment_fav_on_click, null)
        builder.setView(view2)
        var dialog: Dialog = builder.create()
        dialog.show()

        if (item.like == "1") {
            Log.d("like", "yes")
            val imgResource = R.drawable.ic_selected_favorite_white
            view2.fav.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0)
        }

        view2.fav_lay.setOnClickListener {

            apiCall((ApiCall.instance.retrofitClient()?.create(RedditAPI::class.java))?.GetMark(LoginDataUser.instance.token!!, LoginDataUser.instance.QuoteID), "like")
            dialog.dismiss()
        }
        view2.Ref_lay.setOnClickListener {
            LoginDataUser.instance.Quotes = listOfQuote.get(pos!!).text
            LoginDataUser.instance.BackGround = listOfQuote.get(pos!!).background
            LoginDataUser.instance.QuoteID = listOfQuote.get(pos!!).id
            dialog.dismiss()
            (context as FragmentActivity).supportFragmentManager.beginTransaction()
                    ?.replace(R.id.container, FragmentDashboardReflection())
                    ?.addToBackStack("Dashboard Page")
                    ?.commit()
        }
        view2.shareIt_lay.setOnClickListener {

            shareIt()

        }
    }

    fun apiCall(call: Call<ResponseBody>?,s: String)
    {
        call?.enqueue(object : Callback<ResponseBody>
        {
            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?)
            {
                Log.d("Api Favourite", "Server Response: " + response.toString());
                try {
                    var json: String? = null
                    json = response?.body()?.string()
                    Log.d("Json Object ", "${json}")
                    if (json == null) {
                        Log.d("Api Like Hit ", " Error Ocuured")
                    } else {
                        if (s.equals("Allfav")) {
                            val cast: JSONArray = JSONObject(json).getJSONArray("data");
                            Log.d("Length is ", "${cast.length()}")
                            for (n in 0 until cast.length()) {
                                val obj = cast.getJSONObject(n).getJSONObject("quote")
                                listOfQuote.add(PojoAllQuoteDetailReflection(obj.getString("_id"), obj.getString("text"), obj.getString("background"), obj.getString("favorite"), " "))
                            }
                            adapter = CustomAdapterSideMenuFaa(listOfQuote, context!!, object : CustomAdapterSideMenuFaa.OnItemClickListener {
                                override fun onItemClick(position: Int) {
                                    Log.d("Clicked 1 ok", "yes ${position}")
                                    LoginDataUser.instance.chnageBackgroundPos = position
                                    LoginDataUser.instance.QuoteID = listOfQuote.get(position).id
                                    pos = position
                                    customAdapterSetting(context, listOfQuote.get(position))
                                }
                            })
                            val manager = GridLayoutManager(context, 1)
                            recyclerviewfavourite.layoutManager = manager

                            recyclerviewfavourite.adapter = adapter

                            loading_indicatefav.visibility = View.INVISIBLE
                        }
                        if (s.equals("like")) {
                            Log.d("Api(like/dislike)", "Server Response: " + response.toString());
                            if (JSONObject(json).get("success").toString().toInt() == 1) {
                                adapter?.removeItem(pos!!)
                            }
                        }


                    }
                }
                catch (e: JSONException) {
                    Log.e("Api Favourite", "Server Response(Json Exception Occur): " + e)
                }
                catch (e: IOException) {
                    Log.e("Api Favourite", "Server Response(IO Exception Occur): " + e)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Api Favourite", "Server Response(On Failure): " +t)
            }
        })
    }

    private fun shareIt() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.setType("text/plain");
        val shareBody = "Here is the share content body"
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        context?.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


}




