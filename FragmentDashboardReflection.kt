package com.developer.rohal.mantra


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.frgament_recycler_view_reflection.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.IOException
class FragmentDashboardReflection : Fragment() {
    var Getview:View?=null
     var loader=Loader()
     var use=ReuseMethod()
     lateinit var currentFragment:FragmentDashboardReflection

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frgament_recycler_view_reflection_1, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        currentFragment=this
        fragment.setOnClickListener {
            it.hideKeyboard()
        }
        Refback.setOnClickListener {
            activity?.onBackPressed()
        }
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        addReflectionRecyclerView.setImageURI(Uri.parse("http://139.59.18.239:6010/mantrame/"+LoginDataUser.instance.BackGround),context)
        text.setText(Html.fromHtml(LoginDataUser.instance.Quotes))
        Refdel.setOnClickListener {
            Toast.makeText(context,"You hace clicked on delete button",Toast.LENGTH_SHORT).show()
        }
        Refok.setOnClickListener {
            if(addReflectiontext.text.isEmpty() || addReflectiontext.text.trim().length==0)
            {
               ReuseMethod().showSnakBar(it,"Empty Reflection")
            }
            else
            {
                Getview=it
                loading_indicateref.visibility=View.VISIBLE
                apiCall((ApiCall.instance.retrofitClient()?.create(RedditAPI::class.java))?.addReflection(LoginDataUser.instance.token!!,LoginDataUser.instance.QuoteID,addReflectiontext.text.toString()),"setRef")
            }
            }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        apiCall((ApiCall.instance.retrofitClient()?.create(RedditAPI::class.java))?.getReflection(LoginDataUser.instance.token!!,LoginDataUser.instance.QuoteID),"getRef")
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
    fun apiCall(call: Call<ResponseBody>?, s: String)
    {
        call?.enqueue(object : Callback<ResponseBody>
        {
            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?)
            {
                Log.d("Api RecyclerView Ref", "Server Response: " + response.toString());
                try {
                    var json: String? = null
                    json = response?.body()?.string()
                    Log.d("Json Object ","${json}")
                    if (json == null)
                    {
                        Log.d("Api RecyclerView Ref"," Error Ocuured")
                    }
                    else
                    {
                        if(s.equals("getRef"))
                        {
                            Log.d("Api(getRef)", "Server Response: " + response.toString());
                            if(JSONObject(json).get("success").toString().toInt()==1) {
                                val raff=JSONObject(json).getJSONObject("data").get("reflection")
                                Log.d("Data",raff.toString())
                                if(raff==null)
                                {

                                }
                                else
                                {
                                    try {
                                        addReflectiontext.setText(raff.toString(),TextView.BufferType.EDITABLE)
                                    }
                                    catch (e:Exception)
                                    {

                                    }

                                }

                            }
                            else
                            {

                            }
                        }
                        else if(s.equals("like"))
                        {
                            Log.d("Api(like/dislike) ", "Server Response: " + response.toString());
                            if(JSONObject(json).get("success").toString().toInt()==1) {
                                if (JSONObject(json).get("message").toString().equals("Favorite removed successfully")) {
                                    favouriteRecyclerView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_selected_favorite_white, 0, 0, 0)
                                } else {
                                    favouriteRecyclerView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_grey, 0, 0, 0)
                                }
                            }
                        }
                        else if(s.equals("setRef"))
                        {
                            use.showSnakBar(Getview,"Sucessfully Reflection Change")
                            activity?.onBackPressed()

                        }

                    }


                }
                catch (e: JSONException) {
                    Log.e("Api RecyclerView Ref", "Server Response(Json Exception Occur): " + e)
                }
                catch (e: IOException) {
                    Log.e("Api RecyclerView Ref", "Server Response(IO Exception Occur): " + e)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Api RecyclerView Ref", "Server Response(On Failure): " +t)
                loading_indicateref.visibility=View.INVISIBLE
            }
        })
    }
}
