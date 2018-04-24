package com.developer.rohal.mantra


import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.android.synthetic.main.fragment_login.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.IOException
import java.util.HashMap
import java.util.regex.Pattern


/**
 * A simple [Fragment] subclass.
 */
class fragmentForgotPassword : Fragment()
{
    var loader=Loader()
    var use=ReuseMethod()
    val EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-    Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //var titleFont = Typeface.createFromAsset(context?.assets, "fonts/Billy Ohio.ttf")
        //forgotname.setTypeface(titleFont)
        forgotBackButton.setOnClickListener {
            activity?.onBackPressed()
        }
        btnSend.setOnClickListener {
            var status: Boolean = true
            if (txtForgotEmail.text.isEmpty() || (!validEmail(txtForgotEmail.text.toString()))) {
                status = false
                if(txtForgotEmail.text.trim().isEmpty()) {
                    //Toast.makeText(context, "Invalid Email", Toast.LENGTH_SHORT).show()
                    var snackbar1: Snackbar = Snackbar.make(it, "Please Enter email!", Snackbar.LENGTH_SHORT);
                    snackbar1.show();
                }
                else
                {
                    var snackbar1: Snackbar = Snackbar.make(it, "Please Enter valid email!", Snackbar.LENGTH_SHORT);
                    snackbar1.show();
                }
            }
            if(status==true)
            {
                loader.ShowCustomLoader(it.context)
                var client = ApiCall()
                var retrofit = client.retrofitClient()
                val name = txtForgotEmail.text.toString().trim()
                val redditAP = retrofit?.create(RedditAPI::class.java)
                var obj=PasswordChange(name)
                var call = redditAP?.changePassword(obj)
                call?.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                        Log.d("server", "onResponse: Server Response: " + response.toString());

                        try {
                            var json: String? = null
                            json = response?.body()?.string()
                            if (json == null) {
                                Log.d("String", "yes")
                                loader.HideCustomLoader()
                                use.showSnakBar(it, "Please enter valid email")
                            } else {

                                Log.d("login", "onResponse: json: " + json);
                                var data: JSONObject? = null;
                                data = JSONObject(json);
                                var data1 = data.get("success")
                                if(data1==1)
                                {
                                    loader.HideCustomLoader()
                                    use.showSnakBar(it, "An e-mail has been sent to ${name} with further instructions.")
                                }

                            }


                        } catch (e: JSONException) {
                            Log.e("JSONException", "onResponse: JSONException: ");
                            loader.HideCustomLoader()
                        } catch (e: IOException) {
                            Log.e("IOexception", "onResponse: JSONException: ");
                            loader.HideCustomLoader()
                        }

                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("OnFailure", "onFailure: Something went wrong: ")
                        loader.HideCustomLoader()
                        use.showSnakBar(it, "Please Check Internet Connection")
                    }
                })
            }
        }
        txtForgotEmail.requestFocus()
        this.activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        taskForgotPassword.setOnClickListener {
            it.hideKeyboard()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_forgot_password, container, false)
    }

    fun validEmail(email: String): Boolean {
        var pattern: Pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
