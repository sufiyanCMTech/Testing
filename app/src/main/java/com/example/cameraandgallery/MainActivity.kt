package com.example.cameraandgallery

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.cameraandgallery.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    var captureimage:CaptureImage?=null
    private val REQUEST_CAMERA=102
    private  val REQUEST_GALLERY=103
    lateinit var bilding:ActivityMainBinding
    var actualpath=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bilding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(bilding.root)
        captureimage = CaptureImage(this)
        bilding.button.setOnClickListener {
            getimage()
        }
        println("thank you so much for proving me tutorial ")
    }

    fun getimage(){
        val item : Array<CharSequence>
        try {
            item = arrayOf("take photo","Chooce Image","Cancel")
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Select Image")
            dialog.setCancelable(false)
            dialog.setItems(item){ dialogInterface, i ->
                if(item[i] =="take photo"){
                    com.github.florent37.runtimepermission.RuntimePermission.askPermission(this)
                        .request(android.Manifest.permission.CAMERA)
                        .onAccepted{
                            takecamera()
                        }.onDenied{
                            AlertDialog.Builder(this)
                                .setMessage("Please Accept the Permission")
                                .setPositiveButton("yes"){ dialogInterface: DialogInterface, i: Int ->
                                    it.askAgain()
                                }
                                .setNegativeButton("No"){ dialogInterface: DialogInterface, i: Int ->
                                    dialogInterface.dismiss()
                                }.show()
                        }.ask()
                }else if (item[i]=="Chooce Image"){
                    com.github.florent37.runtimepermission.RuntimePermission.askPermission(this)
                        .request(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .onAccepted{
                            Fromgallery()
                        }.onDenied{
                            AlertDialog.Builder(this)
                                .setMessage("Please Accept the Permission")
                                .setPositiveButton("Yes"){ dialogInterface: DialogInterface, i: Int ->
                                    it.askAgain()
                                }.setNegativeButton("No"){ dialogInterface: DialogInterface, i: Int ->
                                    dialogInterface.dismiss()
                                }.show()
                        }.ask()

                } else{
                    dialogInterface.dismiss()
                }
            }
            dialog.show()

        }catch(e:Exception){
            e.printStackTrace()

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== REQUEST_CAMERA){
            getimage(captureimage?.getRightAngleImage(captureimage?.imagePath).toString())
        }else if (requestCode == REQUEST_GALLERY){
            getimage(captureimage?.getRightAngleImage(captureimage?.getPath(data?.data,this)).toString())

        }
    }
    private fun takecamera(){
        val intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT,captureimage?.setImageUri())
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, REQUEST_CAMERA)
    }
    private fun Fromgallery(){
        val intent=Intent()
        intent.type="image/*"
        intent.action= Intent.ACTION_PICK
        startActivityForResult(intent, REQUEST_GALLERY)
    }
    fun getimage(path:String){
        getpath(captureimage?.decodeFile(path))
        Log.d("main","{$path}")
    }
    fun getpath(bitmap: Bitmap?){
        val temp:Uri= captureimage!!.getImageUri(this,bitmap)
         actualpath= captureimage?.getRealPathFromURI(temp,this).toString()
        Log.d("Sufiyan"," actualpath $actualpath")
        Glide.with(this)
            .load(actualpath)
            .into(bilding.imageView)
    }



    }


