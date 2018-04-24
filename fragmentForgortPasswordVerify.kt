package com.developer.rohal.mantra


import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.fragment_forgort_password_verify.*
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap


/**
 * A simple [Fragment] subclass.
 */
class fragmentForgortPasswordVerify : Fragment() {
    private val SPLASH_TIME_OUT = 1000
    var load=Loader()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var titleFont = Typeface.createFromAsset(context?.assets, "fonts/Billy Ohio.ttf")
        forgotVerfiyApp.setTypeface(titleFont)
        forgotVerfiyBackButton.setOnClickListener {
            activity?.onBackPressed()
        }
        this.activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        btnResetPassword.setOnClickListener {
            var status: Boolean = true
            if (txtVerifyCode.text.trim().isEmpty()) {
                status = false
                //Toast.makeText(context, "Invalid Email", Toast.LENGTH_SHORT).show()
                var snackbar1: Snackbar = Snackbar.make(it, "Enter Code!", Snackbar.LENGTH_SHORT);
                snackbar1.show();
            }
            else if(txtForgotVerifyNewPassword.text.trim().isEmpty())
            {
                status = false
                //Toast.makeText(context, "Invalid Password", Toast.LENGTH_SHORT).show()
                var snackbar1: Snackbar = Snackbar.make(it, "Enter New Password!", Snackbar.LENGTH_SHORT);
                snackbar1.show();
            }
            else if(status==true) {

                var status1=" "
                var client = ApiCall()
                var retrofit = client.retrofitClient()
                val redditAP = retrofit?.create(RedditAPI::class.java)
                val headerMap = HashMap<String, RequestBody>()
                headerMap.put("currentPassword", RequestBody.create(MediaType.parse("text/plain"),"${txtVerifyCode.text.trim()}"))
                headerMap.put("newPassword", RequestBody.create(MediaType.parse("text/plain"),"${txtForgotVerifyNewPassword.text.trim()}"))

                var call=redditAP?.changePassword(LoginDataUser.instance.token!!,headerMap)
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
                                    activity?.onBackPressed()
                                }, SPLASH_TIME_OUT.toLong())

                            }
                            else{
                                var view=ReuseMethod()
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {

                    }

                })
            }

        }
        taskForgotVerfiy.setOnClickListener {
            it.hideKeyboard()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_forgort_password_verify, container, false)
    }
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
