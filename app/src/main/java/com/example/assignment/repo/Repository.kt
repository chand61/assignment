package com.example.assignment.repo

import android.Manifest
import android.app.Application
import androidx.annotation.RequiresPermission
import com.example.assignment.model.ContactModel
import com.example.assignment.utils.getContacts

class Repository(private val context: Application) {


    @RequiresPermission(Manifest.permission.READ_CONTACTS)
    fun getDeviceContacts () : ArrayList<ContactModel>
    {
      return  getContacts(context) as ArrayList<ContactModel>
    }

    companion object
    {
        var repository: Repository?=null
        @Synchronized
        fun getInstance(context: Application): Repository? {
            if (repository == null) repository = Repository(context)
            return repository
        }
    }
}