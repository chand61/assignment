package com.example.assignment

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.assignment.model.ContactModel
import com.example.assignment.repo.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainViewModel( private val repository: Repository) : ViewModel() {
    var deviceContacts: MutableLiveData<ArrayList<ContactModel>> = MutableLiveData(arrayListOf())

    @SuppressLint("MissingPermission")
    suspend fun getDeviceContacts() {
        val contact = repository.getDeviceContacts()
        deviceContacts.postValue(contact)
    }
}