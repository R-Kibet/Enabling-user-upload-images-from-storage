@file:Suppress("DEPRECATION")

package com.example.anew

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anew.models.BoardSize
import com.example.anew.utils.EXTRA_SIZE
import com.example.anew.utils.isPermissionGranted
import com.example.anew.utils.requestPermission

class CustomActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "CustomActivity"
        private const val PICK_PHOTO_CODE = 156
        private const val READ_EXTERNAL_PHOTOS_CODE = 157
        private const val READ_PHOTOS_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

    //reference views
    private lateinit var imageSelect: RecyclerView
    private lateinit var btnSave: Button
    private lateinit var gameName: EditText
    private lateinit var boardSize: BoardSize
    private lateinit var adapter: imageSelectAdapter

    private var imagesRequired = -1
    private var chosenImage = mutableListOf<Uri>() // Uri - Uniform Resource Identifier - string that identifies where a particular resource leaves
                                                  //resource is image stored on the phone


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom)


        //initialize referemces
        imageSelect = findViewById(R.id.imageSelect)
        btnSave = findViewById(R.id.btnSave)
        gameName = findViewById(R.id.gameName)

        //pulling data out from intent and cast it
        boardSize =  intent.getSerializableExtra(EXTRA_SIZE) as BoardSize

        imagesRequired =boardSize.getPairs()

        supportActionBar?.title = "choose the number (0/ $imagesRequired)"

        //setting up back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //contains 2 core components as the main activity
        //adapter and layout manager
        imageSelect.layoutManager = GridLayoutManager(this, boardSize.getWidth())
        imageSelect.setHasFixedSize(true)

        //adapter

        //it will take 3 parameters [Context (this) , list of chosen images through uri ,boardsize]
         adapter = imageSelectAdapter(this, chosenImage, boardSize,  object: imageSelectAdapter.ImageClickListener{
            override fun onPlaceHolderCLicked() {
                //user needs to allow  access for the intent to launch
                if (isPermissionGranted(this@CustomActivity,READ_PHOTOS_PERMISSION)) {

                    launchIntentForPhotos()//implicit intent -request to perform an action based on desired action

                }else {
                    requestPermission(this@CustomActivity, READ_PHOTOS_PERMISSION, READ_EXTERNAL_PHOTOS_CODE)
                }
            }

        })

        imageSelect.adapter = adapter
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == READ_EXTERNAL_PHOTOS_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                launchIntentForPhotos()
        }else{
            Toast.makeText(this, "Access is required to ur galery" , Toast.LENGTH_LONG)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }



    //initializing the home button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) { //contains android prefix as it was added
            finish()

            return true
        }
        return  super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //from the parameters
        if (requestCode != PICK_PHOTO_CODE || resultCode != Activity.RESULT_OK || data == null ){
            Log.w(TAG, "No data from activity user cancelled " )
            return
        }

        //2 attributes single and multiple data
        val singleUri = data.data
        val clipData = data.clipData //multiple images

        if (clipData != null) {
            Log.i (TAG,  "Clip Dat number of images ${clipData.itemCount}: $clipData")

            // iterate through the clip data
            for (i in 0 until clipData.itemCount){
                val clipItem = clipData.getItemAt(i)
                if (chosenImage.size < imagesRequired) {
                    chosenImage.add(clipItem.uri)
                }
            }
        }else if (singleUri != null){
            Log.i (TAG, "data: $singleUri")
            chosenImage.add(singleUri)
        }
        adapter.notifyDataSetChanged()
        supportActionBar?.title = "Choose images ${chosenImage.size}/$imagesRequired"
    }

    private fun launchIntentForPhotos() {
        val intent = Intent(Intent.ACTION_PICK) //implicit intent hence the action pick
        intent.type ="image/*" //describing only pick image
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // allows user to pick multiple images
        startActivityForResult(Intent.createChooser(intent ,"choose Images"),PICK_PHOTO_CODE)
        // for this to function we need to give access/permission in manifest
    }



}