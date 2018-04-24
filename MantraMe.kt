package com.developer.rohal.mantra

import android.content.Context
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class MantraMe
{
    fun FilePicture(context:Context): File
    {
        var f: File = File(context?.cacheDir, "Saii.png")
        f.createNewFile()
        var bitmap: Bitmap? =LoginDataUser.instance.FacebookGooglebitmap
        var bos: ByteArrayOutputStream = ByteArrayOutputStream();
        bitmap?.compress(Bitmap.CompressFormat.PNG, 0, bos);
        var bitmapdata = bos.toByteArray();
        var fos: FileOutputStream = FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
        return f
    }
}