package com.example.assignment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment.adapter.ContactAdapter
import com.example.assignment.databinding.ActivityMainBinding
import com.example.assignment.repo.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        if(checkPermission())
        {
            init()
            getDeviceContacts()
            initRecyclerView()
        }else
        {
            requestPermission()
        }

    }

    private fun init() {
        val repository = Repository.getInstance(application)
        val factory = MainFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    private fun getDeviceContacts ()
    {
        CoroutineScope(IO).launch {
        viewModel.getDeviceContacts()
            withContext(Main)
            {
                binding.progress.visibility = View.GONE
            }
        }
    }

    private fun initRecyclerView() {
        binding.contactRecycler.layoutManager = LinearLayoutManager(this)
        adapter = ContactAdapter()
        binding.contactRecycler.adapter = adapter
        displayPhoneContactsList()
    }

    private fun displayPhoneContactsList() {
        viewModel.deviceContacts.observe(this, Observer {
           if(it.size>0)
           {
               adapter.setList(it)
               adapter.notifyDataSetChanged()
           }
        })
    }


    private fun checkPermission(): Boolean
    {
          return  ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), 2)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            2 -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init()
                getDeviceContacts()
                initRecyclerView()
            }
            else {
               requestPermission()
            }
        }

    }

}