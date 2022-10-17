package com.example.assignment.utils

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.assignment.R

@BindingAdapter("image_src")
fun setImageUri(view: ImageView, path: Uri?) {
    if (path != null) {
        Glide.with(view.context).load(path).placeholder(R.drawable.no_dp).into(view)
    } else {
        Glide.with(view.context).load(R.drawable.no_dp).into(view)
    }
}

@BindingAdapter("set_numbers")
fun setText(view: TextView, numbers: List<String>?) {

    if (numbers != null && numbers.isNotEmpty()) {
        view.text = numbers.toString().replace("[","").replace("]","")
    } else {
        view.text = "N/A"
    }
}

@BindingAdapter("set_emails")
fun setEmails(view: TextView, emails: List<String>?) {

    if (emails != null && emails.isNotEmpty()) {
        view.text = emails.toString().replace("[","").replace("]","")
    } else {
        view.text = "N/A"
    }
}

@BindingAdapter("set_websites")
fun setWebsites(view: TextView, websites: List<String>?) {

    if (websites != null && websites.isNotEmpty()) {
        view.text = websites.toString().replace("[","").replace("]","")
    } else {
        view.text = "N/A"
    }
}

