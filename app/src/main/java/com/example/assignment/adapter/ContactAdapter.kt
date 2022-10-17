package com.example.assignment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.R
import com.example.assignment.databinding.RecyclerDesignBinding
import com.example.assignment.model.ContactModel


class ContactAdapter(
) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    private val contactsList = ArrayList<ContactModel>()
    private lateinit var binding: RecyclerDesignBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.recycler_design, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contactsList[position])

    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    fun setList(contacts: List<ContactModel>) {
        contactsList.clear()
        contactsList.addAll(contacts)

    }

    class ViewHolder(private val binding: RecyclerDesignBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: ContactModel?)
        {
                binding.contact = contact
                binding.executePendingBindings()
        }
    }
}