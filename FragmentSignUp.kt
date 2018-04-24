package com.developer.rohal.mantra


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.facebook.drawee.backends.pipeline.Fresco
import kotlinx.android.synthetic.main.dialog_box.view.*
import kotlinx.android.synthetic.main.fragment_fragment_sign_up.*
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*
import java.util.HashMap
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class FragmentSignUp:Fragment() {
    var f: File? = null
    private val PICK_FROM_FILE = 300
    var bmpuri: Bitmap? = null
    private var mCapturedImageURI: Uri? = null
    val REQUEST_IMAGE_CAPTURE = 1
    private var url_pic: Uri? = null
    var bmpString: String = " "
    var bmpByteArray: ByteArray? = null
    val EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-    Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val stringFirstPart = "By Clicking Continue you agree to our Terms & Conditions"
        val stringNext: String = "\nand Privacy Policies"
        LoginDataUser.instance.content = 9
        pic.setOnClickListener {
            Dialog_box(context)
        }
        btn_back.setOnClickListener {
            val transaction = fragmentManager?.beginTransaction()
            fragmentManager?.popBackStack()
            transaction?.remove(this)
            transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            transaction?.replace(R.id.container, fragmentLogin())
            transaction?.commit()
        }
        var ssb: SpannableStringBuilder = SpannableStringBuilder();
        ssb.append(stringFirstPart + stringNext)
        ssb.setSpan(object : ClickableSpan() {
            override fun onClick(v: View) {
                Log.d("Clinked", "Terms and Conditions")
                //Toast.makeText(v.context, "Terms and Conditions", Toast.LENGTH_SHORT).show()
                var snackbar1: Snackbar = Snackbar.make(v, "Terms and Conditions!", Snackbar.LENGTH_SHORT);
                snackbar1.show();
            }
        }, 38, 56, 0)
        ssb.setSpan(UnderlineSpan(), 38, 56, 0)
        ssb.setSpan(ForegroundColorSpan(Color.CYAN), 38, 56, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(ForegroundColorSpan(context?.getResources()?.getColor(R.color.SignUpColor)!!), 38, 56, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(RelativeSizeSpan(1.1f), 38, 56, 0)
        ssb.setSpan(object : ClickableSpan() {
            override fun onClick(v: View) {
                Log.d("Clinked", "Privacy Policies")
                //Toast.makeText(v.context, "Privacy Policies", Toast.LENGTH_SHORT).show()
                var snackbar1: Snackbar = Snackbar.make(v, "Privacy Policies!", Snackbar.LENGTH_SHORT);
                snackbar1.show();
            }
        }, 61, ssb.length, 0)
        ssb.setSpan(UnderlineSpan(), 61, ssb.length, 0)
        ssb.setSpan(ForegroundColorSpan(Color.CYAN), 61, ssb.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(ForegroundColorSpan(context?.getResources()?.getColor(R.color.SignUpColor)!!), 61, ssb.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(RelativeSizeSpan(1.1f), 61, ssb.length, 0)
        yess.append(ssb)
        yess.setMovementMethod(LinkMovementMethod.getInstance());
        btnSignUp.setOnClickListener {
            if (txtSignUpUsername.text.trim().isEmpty() || txtSignUpUsername.text.length == 0) {
                var snackbar1: Snackbar = Snackbar.make(it, "Please enter your username", Snackbar.LENGTH_SHORT);
                snackbar1.show();
            } else if (txtSignUpEmialAddress.text.isEmpty() || txtSignUpEmialAddress.text.length == 0 || !validEmail(txtSignUpEmialAddress.text.toString())) {
                if (txtSignUpEmialAddress.text.isEmpty()) {
                    var snackbar2: Snackbar = Snackbar.make(it, "Please enter your emial id", Snackbar.LENGTH_SHORT);
                    snackbar2.show();
                } else if (txtSignUpEmialAddress.text.trim().isEmpty()) {
                    var snackbar1: Snackbar = Snackbar.make(it, "Please enter your emial id!", Snackbar.LENGTH_SHORT);
                    snackbar1.show();
                } else if (!validEmail(txtSignUpEmialAddress.text.toString())) {
                    var snackbar1: Snackbar = Snackbar.make(it, "Please enter a valid emial-id", Snackbar.LENGTH_SHORT);
                    snackbar1.show();
                }

            } else if (txtSignUpPassword.text.isEmpty() || txtSignUpEmialAddress.text.length == 0) {
                if (txtSignUpPassword.text.isEmpty()) {
                    var snackbar2: Snackbar = Snackbar.make(it, "Please enter your password", Snackbar.LENGTH_SHORT);
                    snackbar2.show();
                } else if (txtSignUpPassword.text.trim().isEmpty()) {
                    var snackbar2: Snackbar = Snackbar.make(it, "Please enter your password", Snackbar.LENGTH_SHORT);
                    snackbar2.show();
                }
            } else if (txtSignUpConfirmPassword.text.isEmpty()) {
                var snackbar1: Snackbar = Snackbar.make(it, "Please re-enter your password", Snackbar.LENGTH_SHORT);
                snackbar1.show();
            } else {
                var pass1 = txtSignUpConfirmPassword.text.trim()
                var pass2 = txtSignUpPassword.text.trim()
                if (pass1 == pass2) {

                }
                else
                {
                    var use = ReuseMethod()
                    var pass = txtSignUpConfirmPassword.text.trim().toString()
                    var confirm = txtSignUpPassword.text.trim().toString()
                    if (pass.equals("${confirm}")) {
                        var loader = Loader()
                        loader.ShowCustomLoader(it.context)
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
                        val name = txtSignUpUsername.text.toString().trim()
                        val email = txtSignUpEmialAddress.text.toString().trim()
                        val password = txtSignUpPassword.text.toString().trim()
                        val confirmPassword = txtSignUpConfirmPassword.text.toString().trim()
                        val redditAP = retrofit.create(RedditAPI::class.java)
                        val headerMap = HashMap<String, RequestBody>()
                        headerMap.put("name", RequestBody.create(MediaType.parse("text/plain"), name))
                        headerMap.put("email", RequestBody.create(MediaType.parse("text/plain"), email))
                        headerMap.put("password", RequestBody.create(MediaType.parse("text/plain"), password))
                        headerMap.put("confirmPassword", RequestBody.create(MediaType.parse("text/plain"), confirmPassword))
                        //var query: String = "profilePic\";filename=\"${f?.absolutePath}"
                        var query: String = "profilePic\";filename=\"${FilePicture().absolutePath}"
                        headerMap.put(query, RequestBody.create(MediaType.parse("/Images"), FilePicture()))           ///

                        var call = redditAP.uploadFileWithPartMap(headerMap)
                        var use = ReuseMethod()
                        call.enqueue(object : Callback<ResponseBody> {
                            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                                Log.d("server", "onResponse: Server Response: " + response.toString());

                                try {
                                    val json: String = response?.body()!!.string();
                                    Log.d("JSON", "onResponse: json: " + json);
                                    var data: JSONObject? = null;
                                    data = JSONObject(json);
//                            Log.d(TAG, "onResponse: data: " + data.optString("json"));
                                    data = JSONObject(json)
                                    var status = data.get("success")

                                    //Log.d("detial","Emial is ${emial} First Name ${firstName} Phone Number  ${phoneNumber} User Pic is ${pic} Country is ${country}")
                                    Log.d("StatusBar", "${status}")
                                    if (status == 0) {

                                        use.showSnakBar(it, "User Exists Error")
                                        loader.HideCustomLoader()
                                    } else {
                                        use.showSnakBar(it, "Successfully Account Created!!")
                                        loader.HideCustomLoader()
                                        val transaction = fragmentManager?.beginTransaction()
                                        transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                                        var fragmentLogin = FragmentDashboard()
                                        transaction?.replace(R.id.container, fragmentLogin)
                                        transaction?.addToBackStack("Login Page")
                                        transaction?.commit()
                                    }
                                } catch (e: JSONException) {
                                    Log.e("JSONException", "onResponse: JSONException: ");
                                    use.showSnakBar(it, "JSONException")
                                    loader.HideCustomLoader()
                                } catch (e: IOException) {
                                    Log.e("IOexception", "onResponse: JSONException: ");
                                    use.showSnakBar(it, "JSONException")
                                    loader.HideCustomLoader()
                                }

                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                Log.e("OnFailure", "onFailure: Something went wrong: ");
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                use.showSnakBar(it, "Something went wrong")
                                loader.HideCustomLoader()
                            }
                        })

                        //
                    } else {
                        //Toast.makeText(it.context, "Invalid password", Toast.LENGTH_SHORT).show()
                        var snackbar1: Snackbar = Snackbar.make(it, "Passsword mismatch", Snackbar.LENGTH_SHORT);
                        snackbar1.show();
                        use.showSnakBar(it, "Mismatch Password")
                    }

                }
            }
            main.setOnClickListener {
                it.hideKeyboard()
            }

            this.activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            //this.activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Fresco.initialize(context)
        return inflater!!.inflate(R.layout.fragment_fragment_sign_up, container, false)
    }

    fun gallery() {
        var intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI)


        Log.d("String", "Yes")
        startActivityForResult(intent, PICK_FROM_FILE)
        Log.d("String", "YesYes")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("yes", "yesyesyesyes")

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            var extras = data?.extras
            var imageBitmap = extras?.get("data") as Bitmap
            var file: File? = null
            file = FilePicture(imageBitmap)
            Log.d("uri", "${Uri.fromFile(file)}")
            pic.setImageURI(Uri.fromFile(file), context)
            bmpuri = imageBitmap
            f = FilePicture(imageBitmap)
            Log.d("uri", "${Uri.fromFile(f)}")
            pic.setImageURI(Uri.fromFile(f), context)
            pic.invalidate();


        } else if (requestCode == PICK_FROM_FILE && resultCode == AppCompatActivity.RESULT_OK) {
            try {
                var mCapturedImageURI: Uri = data?.data!!
                pic.setImageURI(mCapturedImageURI, context)
                bmpuri = MediaStore.Images.Media.getBitmap(context?.getContentResolver(), mCapturedImageURI);
                var imagePath = mCapturedImageURI.path

                Log.d("path", "${mCapturedImageURI}")
            } catch (e: java.lang.Exception) {

                // e.printStackTrace()
                // Toast.makeText(context, "Image_notfound ${e}", Toast.LENGTH_LONG).show()
            }

        }

    }

    fun Dialog_box(view: Context?) {
        var builder: AlertDialog.Builder = AlertDialog.Builder(context)
        var inflater: LayoutInflater = layoutInflater
        var view: View = inflater.inflate(R.layout.dialog_box, null)
        builder.setView(view)
        var Dilog: Dialog = builder.create()
        Dilog.show()
        view.btn_Gallery.setOnClickListener {
            gallery()
            builder.setInverseBackgroundForced(true)
            Dilog.dismiss()
        }
        view.btn_Camera.setOnClickListener {
            dispatchTakePictureIntent()
            Dilog.dismiss()
        }
    }

    private fun dispatchTakePictureIntent() {
        var takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(context?.packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    fun validEmail(email: String): Boolean {
        var pattern: Pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    fun savePicture(bm: Bitmap, imgName: String) {
        var fOut: OutputStream? = null
        val strDirectory = Environment.getExternalStorageDirectory().toString()
        var f = File(strDirectory, imgName)
        try {
            fOut = FileOutputStream(f)

            /**Compress image */
            bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut)
            fOut.flush()
            fOut.close()

            /**Update image to gallery */
            MediaStore.Images.Media.insertImage(context?.contentResolver,
                    f.absolutePath, f.name, f.name)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        MediaScannerConnection.scanFile(context, arrayOf(f.toString()), null
        ) { path, uri ->
            url_pic = uri
            pic.setImageURI(uri, context)
            Log.i("ExternalStorage", "Scanned $path:")

        }
    }

    fun FilePicture(bitmap: Bitmap): File {
        var f: File = File(context?.cacheDir, "${bitmap.toString()}")
        f.createNewFile()
        var bos: ByteArrayOutputStream = ByteArrayOutputStream();
        bitmap?.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        var bitmapdata = bos.toByteArray();
        var fos: FileOutputStream = FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
        return f
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun FilePicture(): File {
        var f: File = File(context?.cacheDir, "Saii")
        f.createNewFile()
//Convert bitmap to byte array
        var bitmap: Bitmap? = bmpuri
        var bos: ByteArrayOutputStream = ByteArrayOutputStream();
        bitmap?.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        var bitmapdata = bos.toByteArray();

//write the bytes in file
        var fos: FileOutputStream = FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();

        return f
    }

}
