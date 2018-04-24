package com.developer.rohal.mantra


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.github.mikephil.charting.data.Entry
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_feedback.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class fragmentFeedback : Fragment() {
    var load=Loader()
    var feedbavkView:View?=null
    private val SPLASH_TIME_OUT = 1000

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val session=Session(context!!)
        var data: JSONObject? = null;
        data = JSONObject("{ \"data\": ${session.getQuote()} }")
        var arrayJson = data.getJSONArray("data")
        for (i in 0 until arrayJson.length()) {
            val actor = arrayJson.getJSONObject(i)
            AllQuotes.instance.data.add(AllQuotes.Quote(
                    actor.getString("id"),
                    actor.getString("text"),
                    actor.getString("background"),
                    actor.getString("like"),
                    actor.getString("Date"),
                    actor.getString("time")
            ))
        }

        for(i in 0 until AllQuotes.instance.data.size-1)
        {
            val item=AllQuotes.instance.data.get(i)
            Log.d("Data is ","${item.text} ${item.id} ${item.background} ${item.Date}")
        }
        feedbackBackButton.setOnClickListener {
            activity?.onBackPressed()
        }
        feedback.setOnClickListener {
            it.hideKeyboard()
        }
        Submit.setOnClickListener {

            if(addReflection.text.trim().isEmpty())
            {
                var snackbar1: Snackbar = Snackbar.make(it, "Empty Feedback", Snackbar.LENGTH_SHORT);
                snackbar1.show()
            }
            else
            {
                feedbavkView=it
                load.ShowCustomLoader(it.context)
                feedBack(addReflection.text.toString())

            }

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_feedback, container, false)
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
    fun feedBack(feedback:String)
    {
        var status1=" "
        var client = ApiCall()
        var retrofit = client.retrofitClient()
        val redditAP = retrofit?.create(RedditAPI::class.java)
        var call=redditAP?.addFeedback(LoginDataUser.instance.token!!,feedback)
        call?.enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?)
            {
                if(response?.isSuccessful!!)
                {
                    var json: String = response?.body()!!.string();
                    Log.d("JSON Alarm", "onResponse: json: " + json);
                    var data: JSONObject? = null;
                    data = JSONObject(json)
                    var status = data.get("success")
                    if(data.get("success")==1 &&data.get("message").equals("Request successful"))
                    {
                        Log.d("status","yes")
                        load.HideCustomLoader()
                        Handler().postDelayed(Runnable
                        {
                            var view=ReuseMethod()
                            view.showSnakBar(feedbavkView,"Successfully feedback added")
                            activity?.onBackPressed()
                        }, SPLASH_TIME_OUT.toLong())

                    }
                    else{
                        var view=ReuseMethod()
                        view.showSnakBar(feedbavkView,"Error")
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {

            }

        })
    }

}// Required empty public constructor
