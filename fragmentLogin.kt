package com.developer.rohal.mantra
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.util.Patterns
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.pop_up_window.view.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import java.util.regex.Pattern
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import java.io.*
import java.net.URL
import java.net.URLConnection

class fragmentLogin: Fragment() {
    // view to provide for Snakbar and loader
    var getview: View? = null
    //
    var use = ReuseMethod()
    var loader = Loader()
    var getView: View? = null
    var url_pic: String = " "
    var name1: String = " "
    var email: String = " "
    var viewContainer: ViewGroup? = null
    private val RC_SIGN_IN = 7
    var status = 0
    val EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-    Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    lateinit var callbackManager: CallbackManager
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        FacebookSdk.sdkInitialize(context?.applicationContext);
        super.onActivityCreated(savedInstanceState)
        //var titleFont = Typeface.createFromAsset(context?.assets, "fonts/Billy Ohio.ttf")
        //name.setTypeface(titleFont)
        var titleForgotPassFont = Typeface.createFromAsset(activity?.assets, "fonts/ariali.ttf")
        btnForgotPassword.setTypeface(titleForgotPassFont)
        var titleNewAccountFont = Typeface.createFromAsset(activity?.assets, "fonts/arialbd.ttf")
        account1.setTypeface(titleNewAccountFont)
        btnSignUpLogin.setOnClickListener {
            val transaction = fragmentManager?.beginTransaction()
            transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            var fragmentLogin = FragmentSignUp()
            transaction?.replace(R.id.container, fragmentLogin)
            transaction?.commit()
        }

        mainLogin.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                p0?.hideKeyboard()
                return true;
            }
        });
        btnLogin.setOnClickListener {
            getView = it
            it.hideKeyboard()
            var status: Boolean = true
            if (txtEmailLogin.text.isEmpty() || (!validEmail(txtEmailLogin.text.toString()))) {
                status = false
                if (txtEmailLogin.text.trim().isEmpty() || txtEmailLogin.text.isEmpty()) {
                    use.showSnakBar(it, "Please enter your email")
                } else {
                    use.showSnakBar(it, "Please  enter valid emial id!")
                }

            } else if (txtPasswordLogin.text.isEmpty()) {
                status = false
                use.showSnakBar(it, "Please enter your password!")

            } else if (status == true) {
                loading_indicator.visibility = View.VISIBLE
                //loader.ShowCustomLoader(it.context)
                var client = ApiCall()
                var retrofit = client.retrofitClient()
                val name = txtEmailLogin.text.toString().trim()
                val password = txtPasswordLogin.text.toString().trim()
                var objects = PojoLoginData(name, password)
                val redditAP = retrofit?.create(RedditAPI::class.java)
                var call = redditAP?.updatePassword(objects)
                call?.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                        Log.d("server", "onResponse: Server Response: " + response.toString());

                        try {
                            var json: String? = null
                            json = response?.body()?.string()
                            if (json == null) {
                                Log.d("String", "yes")
                                loading_indicator.visibility = View.INVISIBLE
                                use.showSnakBar(getView, "Please enter valid username and password")
                            } else {

                                Log.d("login", "onResponse: json: " + json);
                                var data: JSONObject? = null;
                                data = JSONObject(json);
                                var obj = LoginDataUser.instance
                                obj.token = data.getString("token")
                                Log.d("token ", "${obj.token}")
                                var data1 = data.getJSONObject("account")
                                var profile = data1.get("profilePic")
                                var display:String=data1.get("name").toString()
                                Log.d("pic", "${profile}")
                                obj.profiePic = "http://139.59.18.239:6010/mantrame/${profile}"
                                var session = Session(context?.applicationContext!!)
                                session.setLoggedin(true, name, password, obj.token!!, obj.profiePic!!,display)
                                loading_indicator.visibility = View.INVISIBLE
                                txtEmailLogin.getText().clear()
                                txtPasswordLogin.getText().clear()
                                val transaction = fragmentManager?.beginTransaction()
                                transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                                var fragmentLogin = FragmentDashboard()
                                transaction?.replace(R.id.container, fragmentLogin)
                                transaction?.commit()
                            }


                        } catch (e: JSONException) {
                            Log.e("JSONException", "onResponse: JSONException: ");
                            loading_indicator.visibility = View.INVISIBLE
                        } catch (e: IOException) {
                            Log.e("IOexception", "onResponse: JSONException: ");
                            loading_indicator.visibility = View.INVISIBLE
                        }

                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("OnFailure", "onFailure: Something went wrong: ")
                        loading_indicator.visibility = View.INVISIBLE
                        use.showSnakBar(getView, "Please Check Internet Connection")
                    }
                })
            }
        }
        txtEmailLogin.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                txtEmailLogin.clearFocus();
                txtPasswordLogin.requestFocus()
                return@OnKeyListener true
            }
            false
        })
        txtPasswordLogin.setOnClickListener {
            it.hideKeyboard()
            txtPasswordLogin.clearFocus()
            btnLogin.setFocusableInTouchMode(true);
            btnLogin.requestFocus()
        }
        btnForgotPassword.setOnClickListener {
            val transaction = fragmentManager?.beginTransaction()
            transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            var fragmentLogin = fragmentForgotPassword()
            transaction?.replace(R.id.container, fragmentLogin)
            transaction?.addToBackStack("forgot Passowrd")
            transaction?.commit()
        }

        btnFacebook.setOnClickListener {
            getview = it
            status = 1
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
            LoginManager.getInstance().logOut()
        }
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        Log.d("Success", "Success")
                        val accessToken = AccessToken.getCurrentAccessToken()
                        val request = GraphRequest.newMeRequest(accessToken)
                        { `object`, response ->
                            var id = `object`.get("id")
                            val ur1: String = "https://graph.facebook.com/${id}/picture?type=large"
                            name1 = `object`.getString("name")
                            email = `object`.getString("email")
                            LoginManager.getInstance().logOut()
                            Log.d("String", "${ur1} ${name1}")
                            getAsynImage("fb", email, name1, ur1, id.toString())
                        }
                        val parameters = Bundle()
                        parameters.putString("fields", "id,email,name")
                        request.parameters = parameters
                        request.executeAsync()
                        LoginManager.getInstance().logOut()
                    }

                    override fun onCancel() {
                        Log.d("Cancel", "Cancel")
                    }

                    override fun onError(exception: FacebookException) {
                        Log.d("Error", "Error ${exception}")
                    }
                })
        btnGoogle.setOnClickListener {
            getview = it
            Gmail()
        }
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(viewContainer!!.windowToken, 0)
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewContainer = container;
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        return inflater!!.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (status == 1) {
            callbackManager.onActivityResult(requestCode, resultCode, data)
            super.onActivityResult(requestCode, resultCode, data)
        } else {

            if (requestCode == RC_SIGN_IN) {
                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                val result1 = Auth.GoogleSignInApi.getSignInResultFromIntent(data)

                if (result1.isSuccess)
                {
                    Log.d("Result", "yes")
                    val acct = result1.signInAccount
                    var email=acct?.email
                    var name=acct?.displayName
                    var url:String=acct?.photoUrl.toString()
                    if(url.equals("null"))
                    {
                        url="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR2Ah84fe3rGjyYhkBK3B3jLCgyv2Ha4M07454lhydryDL1sVCI"
                    }
                    var id=acct?.id
                    Log.d("Data", "${email} ${name} ${url} ${id}")
                    getAsynImage("gl","${email}","${name}","${url}","${id}")
                } else {
                    Log.d("Error", "yes")
                }

            }
        }
    }

    fun Gmail() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        var mGoogleSignInClientClient = GoogleSignIn.getClient(context!!.applicationContext, gso)
        val account = GoogleSignIn.getLastSignedInAccount(context)
        val signInIntent = mGoogleSignInClientClient.getSignInIntent()
        Log.d("name", "yes")
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun validEmail(email: String): Boolean {
        var pattern: Pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    fun popWindow(view: Context?) {
        var builder: AlertDialog.Builder = AlertDialog.Builder(context)
        var inflater: LayoutInflater = layoutInflater
        var view: View = inflater.inflate(R.layout.pop_up_window, null)
        builder.setView(view)
        builder.setTitle("Recovery Password")
        var Dilog: Dialog = builder.create()
        Dilog.show()
        view.btn_ok.setOnClickListener {
            Toast.makeText(it.context, "Button Pressed with ${view.txt_emial.text}", Toast.LENGTH_SHORT).show()
            Dilog.dismiss()
        }
    }

    fun getAsynImage(type: String, emial: String, name: String, url: String, id: String) {
        var loader = Loader()
        var use = ReuseMethod()
        loader.ShowCustomLoader(getview?.context!!)
        var t: AsyncTask<Void, Void, Bitmap> = object : AsyncTask<Void, Void, Bitmap>() {
            override fun doInBackground(vararg p0: Void?): Bitmap? {
                var bmp: Bitmap? = null;

                try {
                    var aURL: URL = URL("${url}")
                    var conn: URLConnection = aURL.openConnection()
                    conn.setUseCaches(true);
                    conn.connect();
                    val `is` = conn.getInputStream()
                    val bis = BufferedInputStream(`is`)
                    bmp = BitmapFactory.decodeStream(bis);
                    bis.close()
                    `is`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                var use = LoginDataUser.instance
                use.FacebookGooglebitmap = bmp
                loader.HideCustomLoader()
                return bmp;
            }

            override fun onPostExecute(bmp: Bitmap) {
                var file = MantraMe()
                var headerMap = HashMap<String, RequestBody>()
                headerMap.put("name", RequestBody.create(MediaType.parse("text/plain"), name))
                headerMap.put("email", RequestBody.create(MediaType.parse("text/plain"), emial))
                if (type.equals("fb"))
                    headerMap.put("facebookId", RequestBody.create(MediaType.parse("text/plain"), id))
                else
                    headerMap.put("googleId", RequestBody.create(MediaType.parse("text/plain"), id))
                var query: String = "profilePic\";filename=\"${file.FilePicture(context!!).absolutePath}"
                headerMap.put(query, RequestBody.create(MediaType.parse("/Images"), file.FilePicture(context!!)))           ///
                var client = ApiCall()
                var retrofit = client.retrofitClient()
                val redditAP = retrofit?.create(RedditAPI::class.java)
                var call = redditAP?.uploadFileWithPartMap(headerMap)
                var use = ReuseMethod()
                call?.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                        Log.d("server", "onResponse: Server Response: " + response.toString());

                        try {
                            val json: String = response?.body()!!.string();
                            Log.d("JSON", "onResponse: json: " + json);
                            var data: JSONObject? = null;
                            data = JSONObject(json)
                            var status = data.get("success")

                            Log.d("StatusBar", "${status}")
                            if (status == 0) {
                                var temp=data.getJSONObject("error")
                                use.showSnakBar(getview, "${temp.get("name")}")
                                loader.HideCustomLoader()
                            }
                            else if(status==1)
                            {
                                Log.d("login", "onResponse: json: " + json);
                                var obj = LoginDataUser.instance
                                obj.token = data.getString("token")
                                Log.d("token ", "${obj.token}")
                                var data1 = data.getJSONObject("account")
                                var profile = data1.get("profilePic")
                                var user=data1.get("name").toString()
                                Log.d("pic", "${profile}")
                                obj.profiePic = "http://139.59.18.239:6010/mantrame/${profile}"
                                var session = Session(context?.applicationContext!!)
                                session.setLoggedin(true, name, "", obj.token!!, obj.profiePic!!,user)
                                use.showSnakBar(getview, "${data.get("message")}")
                                val transaction = fragmentManager?.beginTransaction()
                                transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                                var fragmentLogin = FragmentDashboard()
                                transaction?.replace(R.id.container, fragmentLogin)
                                transaction?.addToBackStack("Login Page")
                                transaction?.commit()
                                loader.HideCustomLoader()
                            }
                        } catch (e: JSONException) {
                            Log.e("JSONException", "onResponse: JSONException: ");
                            use.showSnakBar(getview, "JSONException")
                            loader.HideCustomLoader()
                        } catch (e: IOException) {
                            Log.e("IOexception", "onResponse: JSONException: ");
                            use.showSnakBar(getview, "JSONException")
                            loader.HideCustomLoader()
                        }

                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("OnFailure", "onFailure: Something went wrong: ");
                        use.showSnakBar(getview, "Internet connection problem")
                        loader.HideCustomLoader()
                    }
                })
            }

        }
        t.execute()
    }


}


