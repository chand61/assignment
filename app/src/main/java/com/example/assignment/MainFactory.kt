package com.example.assignment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.assignment.repo.Repository
import java.lang.IllegalArgumentException

class MainFactory( private val repository: Repository?):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(repository!!) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}