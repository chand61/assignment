package com.example.assignment.model

import android.graphics.Bitmap
import android.net.Uri


data class ContactModel (
    val id: String,
    val name: String?,
    val email:MutableList<String>?,
    val mobileNumber: MutableList<String>?,
    val websites:MutableList<String>?,
    val photoURI: Uri?,
    val birthday:String
)