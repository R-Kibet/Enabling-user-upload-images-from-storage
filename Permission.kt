package com.example.anew.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

//create 2 function isPermissionGranted and requestPermission

//isPermissionGranted
fun isPermissionGranted(context: Context, permission: String): Boolean{
    return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}

fun requestPermission(activity: Activity? , permission : String, requestCode:Int) {
    ActivityCompat.requestPermissions(activity!! , arrayOf(permission),requestCode)
}