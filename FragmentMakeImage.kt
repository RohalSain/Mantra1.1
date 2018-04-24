package com.developer.rohal.mantra


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_make_image.*
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FragmentMakeImage : Fragment()
{
    var mCurrentPhotoPath: String=""
    val REQUEST_IMAGE_CAPTURE = 1
    private val PICK_FROM_FILE = 300
    var Bitmap: Bitmap? = null
    var photoFile : File? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_make_image, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activityCreated()
        Camera.setOnClickListener {
            setupPermissions()
            dispatchTakePictureIntent()
        }
        Gallery.setOnClickListener {
            gallery()
        }
        makeImagefrag.setOnClickListener {
            setHeaderMap()
        }
    }
    fun activityCreated()
    {
        MakeImageText.setText(Html.fromHtml("${LoginDataUser.instance.Quotes}"))
        makeImageCircular.setImageURI(Uri.parse("http://139.59.18.239:6010/mantrame/${LoginDataUser.instance.BackGround}"))
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            Bitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath)
            makeImageCircularImage.visibility=View.VISIBLE
            makeImageCircularImage.setImageBitmap(Bitmap)

        } else if (requestCode == PICK_FROM_FILE && resultCode == AppCompatActivity.RESULT_OK)
        {
            try
            {
                    val mCapturedImageURI: Uri = data?.data!!
                    Bitmap = MediaStore.Images.Media.getBitmap(context?.getContentResolver(), mCapturedImageURI);
                    makeImageCircularImage.visibility=View.VISIBLE
                    makeImageCircularImage.setImageBitmap(Bitmap)
            }
            catch (e: java.lang.Exception)
            {
                e.printStackTrace()
            }

        }

    }


    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File
    {
        val image = File.createTempFile(
                "JPEG_" + SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) + "_", /* prefix */
                ".jpg", /* suffix */
                context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)      /* directory */
        )
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    fun FilePicture(): File {
        var f: File = File(context?.cacheDir, "Saii.png")
        f.createNewFile()
        //Convert bitmap to byte array
        val bos: ByteArrayOutputStream = ByteArrayOutputStream();
        Bitmap?.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
        val bitmapdata = bos.toByteArray();

        //write the bytes in file
        val fos: FileOutputStream = FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
        return f
    }
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try
        {
            photoFile = createImageFile();
        }
        catch ( ex : IOException) {}
        if (photoFile != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,FileProvider.getUriForFile(context!!,
                    "com.developer.rohal.mantra.fileprovider",
                    photoFile!!));
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    fun gallery() {
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_FROM_FILE)
    }
    fun setHeaderMap()
    {
        loading_indicator_makeImage.visibility=View.VISIBLE
        val headerMap = HashMap<String, RequestBody>()
        headerMap.put("quoteID", RequestBody.create(MediaType.parse("text/plain"),LoginDataUser.instance.QuoteID))
        headerMap.put("background\";filename=\"${FilePicture().absolutePath}", RequestBody.create(MediaType.parse("/Images"), FilePicture()))
        apiCall((ApiCall.instance.retrofitClient()?.create(RedditAPI::class.java))?.makeImage(LoginDataUser.instance.token!!,headerMap),"setImage")
    }
    fun apiCall(call: Call<ResponseBody>?, s: String)
    {
        call?.enqueue(object : Callback<ResponseBody>
        {
            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?)
            {
                Log.d("Api Make Image", "Server Response: " + response.toString());
                try {
                    var json: String? = null
                    json = response?.body()?.string()
                    Log.d("Json Object ","${json}")
                    if (json == null)
                    {
                        Log.d("Api Make Image "," Error Ocuured")
                    }
                    else
                    {
                        if(s.equals("setImage"))
                        {
                            Log.d("Api(setImage)", "Server Response: " + response.toString());
                            if(JSONObject(json).get("success").toString().toInt()==1) {
                                val background=JSONObject(json).getJSONObject("data").getString("background")
                                Log.d("Background is ", "${background}")
                                var adapter:DashboardCustomAdapter?=null
                                adapter?.ChangeItem(LoginDataUser.instance.clickedPos!!,background.toString())
                                LoginDataUser.instance.backGroundStatus=true
                                LoginDataUser.instance.backGroundChange=background.toString()
                                activity?.onBackPressed()

                            }
                        }
                    }


                }
                catch (e: JSONException) {
                    Log.e("Api Make Image", "Server Response(Json Exception Occur): " + e)
                    loading_indicator_makeImage.visibility=View.INVISIBLE
                }
                catch (e: IOException) {
                    Log.e("Api Make Image", "Server Response(IO Exception Occur): " + e)
                    loading_indicator_makeImage.visibility=View.INVISIBLE
                }


            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Api Make Image", "Server Response(On Failure): " +t)
                loading_indicator_makeImage.visibility=View.INVISIBLE
            }
        })
    }
     fun setupPermissions() {
        if (ContextCompat.checkSelfPermission(activity!!,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
            Log.i("Rohal", "Permission to record denied")
        }
    }
}