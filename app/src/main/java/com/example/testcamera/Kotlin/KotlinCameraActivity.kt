package com.example.testcamera.Kotlin

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.testcamera.Java.JavaCameraActivity
import com.example.testcamera.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class KotlinCameraActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var btnClickKotlin: Button
    lateinit var imagePhotoKotlin: ImageView

    private val CAMERA_REQUEST_CODE = 100
    private val REQUEST_EXTERNAL_STORAGE_RESULT = 101
    private var mImageFileLocation = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_camera)

        btnClickKotlin = findViewById(R.id.btnClickKotlin)
        imagePhotoKotlin = findViewById(R.id.imagePhotoKotlin)

        btnClickKotlin.setOnClickListener(this)
    }

    private fun callCameraApp() {
        val callCameraApplicationIntent = Intent()
        callCameraApplicationIntent.action = MediaStore.ACTION_IMAGE_CAPTURE
        var photoFile: File? = null
        try {
            photoFile = createImageFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val authorities = applicationContext.opPackageName + ".fileprovider"
        val imageUri = FileProvider.getUriForFile(this, authorities, photoFile!!)
        callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(callCameraApplicationIntent, CAMERA_REQUEST_CODE)
    }

    /**
     * I created function for file, and now we can use a picture on full size.
     */
    @Throws(IOException::class)
    fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFillName = "IMAGE_" + timeStamp + "_"
        val storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFillName, ".jpg", storageDirectory)
        mImageFileLocation = image.absolutePath
        return image
    }

    /**
     * fun reduces images size
     */
    fun setReducesImageSize() {
        val targetImageViewWeight: Int = imagePhotoKotlin.width
        val targetImageViewHeight: Int = imagePhotoKotlin.height
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(mImageFileLocation, bmOptions)
        val cameraImageWight = bmOptions.outWidth
        val cameraImageHeight = bmOptions.outHeight
        bmOptions.inSampleSize = (cameraImageWight / targetImageViewWeight).coerceAtMost(cameraImageHeight / targetImageViewHeight)
        bmOptions.inJustDecodeBounds = false
        val photoReducedSizeBitmap = BitmapFactory.decodeFile(mImageFileLocation, bmOptions)
        imagePhotoKotlin.setImageBitmap(photoReducedSizeBitmap)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE_RESULT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callCameraApp()
            } else {
                Toast.makeText(this, "External write permission has not been granted, cannot saved an image", Toast.LENGTH_LONG).show()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    /**
     * We get a result
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

//            val photoCaptureBitmap = BitmapFactory.decodeFile(mImageFileLocation)
//            imagePhotoKotlin.setImageBitmap(photoCaptureBitmap)


            setReducesImageSize()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            callCameraApp()
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "External storage permission requires t save an image", Toast.LENGTH_LONG).show()
            }
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_EXTERNAL_STORAGE_RESULT)
        }
    }
}
