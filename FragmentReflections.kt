package com.developer.rohal.mantra


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_side_menu_reflection.*
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_adpater_setting.view.*
import kotlinx.android.synthetic.main.fragment_fragment_dashboard.*
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.HashMap
import java.util.concurrent.TimeUnit

class FragmentReflections : Fragment()
{
    var item: PojoAllQuoteDetailReflection?=null
    var listOfQuote:ArrayList<PojoAllQuoteDetailReflection> = ArrayList<PojoAllQuoteDetailReflection>();

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_side_menu_reflection, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        apiCall((ApiCall.instance.retrofitClient()?.create(RedditAPI::class.java))?.getAllReflection(LoginDataUser.instance.token!!), "getAllReflection")
        fragmentSideMenu.setOnClickListener {
            it.hideKeyboard()
        }
                RefCancel.setOnClickListener {
            activity?.onBackPressed()
        }
        Refdel.setOnClickListener {
            var snackbar1: Snackbar = Snackbar.make(it, "Delete Button Pressed!", Snackbar.LENGTH_SHORT);
            snackbar1.show();
        }
        Refok.setOnClickListener {
            apiCall((ApiCall.instance.retrofitClient()?.create(RedditAPI::class.java))?.getAllReflection(LoginDataUser.instance.token!!), "getAllReflection")
            if(listOfQuote.size==0)
            {
                ReuseMethod().showSnakBar(it, "No Reflection added yet")
            }
            else
            {
                apiCall((ApiCall.instance.retrofitClient()?.create(RedditAPI::class.java))?.addReflection(LoginDataUser.instance.token!!,item?.id!!,addReflection.text.toString()), "setReflection")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
    private fun apiCall(call: Call<ResponseBody>?, s: String)
    {
        call?.enqueue(object : Callback<ResponseBody>
        {
            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?)
            {
                Log.d("Api Reflection", "Server Response: " + response.toString())
                try {
                    var json: String? = null
                    json = response?.body()?.string()
                    Log.d("Json Object ","${json}")
                    if (json == null)
                    {
                        Log.d("Api Reflection "," Error Ocuured")
                    }
                    else
                    {
                        if(s.equals("getAllReflection"))
                        {
                            if(JSONObject(json).get("success").toString().toInt()==1)
                            {
                                val cast: JSONArray = JSONObject(json).getJSONArray("data")
                                for (i in 0 until cast.length()) {
                                    listOfQuote.add(PojoAllQuoteDetailReflection (
                                            cast.getJSONObject(i).getJSONObject("quote").getString("_id"),
                                            cast.getJSONObject(i).getJSONObject("quote").getString("text"),
                                            cast.getJSONObject(i).getJSONObject("quote").getString("background"),
                                            cast.getJSONObject(i).getJSONObject("quote").getString("favorite"),
                                            cast.getJSONObject(i).get("reflection").toString() )
                                    )
                                }
                                try
                                {
                                    Quote.text = " "
                                    Quote.text = listOfQuote.get(0).text
                                    image.setImageURI("http://139.59.18.239:6010/mantrame/${listOfQuote.get(0).background}?dim=500x500")
                                    addReflection.setText(listOfQuote.get(0).reflection,TextView.BufferType.EDITABLE)

                                }

                                catch (e: Exception)
                                {

                                }
                                try
                                {

                                    recyclerviewSideMenu.layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL)
                                }
                                catch (e:Exception)
                                {

                                }
                                recyclerviewSideMenu.adapter =CustomAdpaterSideMenuReflection(
                                        listOfQuote, activity?.applicationContext!!,
                                        object : CustomAdpaterSideMenuReflection.OnItemClickListener
                                         {
                                            override fun onItemClick(position: Int)
                                            {

                                              item= listOfQuote.get(position)
                                              Quote.setText(" ")
                                              Quote.setText(listOfQuote.get(position).text)
                                              image.setImageURI("http://139.59.18.239:6010/mantrame/${listOfQuote.get(position).background}?dim=500x500}")
                                              addReflection.setText(listOfQuote.get(position).reflection,TextView.BufferType.EDITABLE)
                                            }
                                         })

                            }

                        }
                        else if(s.equals("setReflection"))
                        {
                            if(JSONObject(json).get("success").toString().toInt()==1) {
                                Log.d("Success ","yes")
                                Handler().postDelayed(Runnable
                                {
                                    activity?.onBackPressed()
                                }, 2000.toLong())
                            }
                        }
                    }
                }
                catch (e: JSONException) {
                    Log.e("Api Reflection", "Server Response(Json Exception Occur): " + e)
                }
                catch (e: IOException) {
                    Log.e("Api Reflection", "Server Response(IO Exception Occur): " + e)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Api Reflection", "Server Response(On Failure): " +t)
            }
        })
    }
}