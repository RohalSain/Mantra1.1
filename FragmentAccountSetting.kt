package com.developer.rohal.mantra


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.renderscript.Allocation
import android.renderscript.ScriptIntrinsicBlur
import android.renderscript.RenderScript
import android.graphics.Bitmap
import android.renderscript.Element
import kotlinx.android.synthetic.main.fragment_account_setting.*
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.WindowManager
import java.net.URL
import android.graphics.drawable.Drawable
import android.os.Handler
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.developer.rohal.mantra.R.styleable.SimpleDraweeView
import java.io.InputStream
import java.lang.System.load
import jp.wasabeef.fresco.processors.BlurPostprocessor
import com.facebook.imagepipeline.request.Postprocessor
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.backends.pipeline.PipelineDraweeController
import kotlinx.android.synthetic.main.dialog_box.view.*
import kotlinx.android.synthetic.main.fragment_forgort_password_verify.*
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.HashMap

class FragmentAccountSetting : Fragment()
{
    var bmpuri: Bitmap? = null
    private val PICK_FROM_FILE = 300
    val REQUEST_IMAGE_CAPTURE = 1
    var bitmap: Bitmap? = null
    var url:String?=null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Imageblur.setImageURI(Session(context!!).getPrfofilePic()+"?dim=500x500")
        account.setImageURI(Session(context!!).getPrfofilePic()+"?dim=500x500")
        url=Session(context!!).getPrfofilePic()
        txtUsernme.setText(Session(context!!).getName(),TextView.BufferType.EDITABLE)
        AccountSettingBackButton.setOnClickListener {
            activity?.onBackPressed()
        }
       Update.setOnClickListener {
           val headerMap = HashMap<String, RequestBody>()
           var name=Session(context!!).getName()
            if(!Session(context!!).getName().equals("${txtUsernme.text}"))
            {
                headerMap.put("name", RequestBody.create(MediaType.parse("text/plain"),"${txtUsernme.text.trim()}"))
            }
           if(!Session(context!!).getPrfofilePic().equals("${url}"))
           {
               headerMap.put("profilePic\";filename=\"${FilePicture().absolutePath}", RequestBody.create(MediaType.parse("/Images"), FilePicture()))
           }
           loading_indicatesetting.visibility=View.VISIBLE
           val call=ApiCall().retrofitClient()?.create(RedditAPI::class.java)?.changePassword(LoginDataUser.instance.token!!,headerMap)
           call?.enqueue(object: Callback<ResponseBody> {
               override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?)
               {
                   if(response?.isSuccessful!!)
                   {
                       val json: String = response?.body()!!.string();
                       var data: JSONObject? =JSONObject(json)
                       if(data?.get("success")==1 &&data.get("message").equals("Request successful"))
                       {
                           loading_indicatesetting.visibility=View.INVISIBLE
                           if(!Session(context!!).getName().equals(txtUsernme.text))
                           {
                               Session(context!!).setName(txtUsernme.text.toString())
                           }
                           if(!Session(context!!).getPrfofilePic().equals("${url}"))
                           {
                             data=data.getJSONObject("account")
                             Session(context!!).setPrfofilePic("http://139.59.18.239:6010/mantrame/${data.getString("profilePic")}")
                           }
                           var snackbar1: Snackbar = Snackbar.make(it, "Updated your Account", Snackbar.LENGTH_SHORT)
                           snackbar1.show()
                           activity?.onBackPressed()
                       }
                       else{

                       }
                   }
               }

               override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                   loading_indicatesetting.visibility=View.INVISIBLE
               }

           })

        }
        this.activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        PicClick.setOnClickListener {
            ChangeImage()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account_setting, container, false)
    }

     private fun ChangeImage()
     {
         val builder: AlertDialog.Builder = AlertDialog.Builder(context)
         val view: View = layoutInflater.inflate(R.layout.photo_box, null)
         builder.setView(view)
         val Dilog: Dialog = builder.create()
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
        if (Intent(MediaStore.ACTION_IMAGE_CAPTURE).resolveActivity(context?.packageManager) != null) {
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_IMAGE_CAPTURE)
        }
    }

    fun gallery() {

        startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), PICK_FROM_FILE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            val extras = data?.extras
            bitmap = extras?.get("data") as Bitmap
            bmpuri = bitmap

        } else if (requestCode == PICK_FROM_FILE && resultCode == AppCompatActivity.RESULT_OK)
        {
            try
            {
                val mCapturedImageURI: Uri = data?.data!!
                url=mCapturedImageURI.toString()
                Imageblur.setImageURI("${mCapturedImageURI}")
                account.setImageURI("${mCapturedImageURI}")
                bitmap = MediaStore.Images.Media.getBitmap(context?.getContentResolver(), mCapturedImageURI);
                bmpuri = bitmap
            }
            catch (e: java.lang.Exception)
            {
                e.printStackTrace()

            }

        }

    }


    fun FilePicture(): File {
        val f: File = File(context?.cacheDir, "Saii.png")
        f.createNewFile()
        val bitmap: Bitmap? = bmpuri
        bitmap?.compress(Bitmap.CompressFormat.PNG, 0, ByteArrayOutputStream())
        FileOutputStream(f).write(ByteArrayOutputStream().toByteArray());
        FileOutputStream(f).flush();
        FileOutputStream(f).close();
        return f
    }
}
