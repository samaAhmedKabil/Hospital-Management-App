package com.example.hospitalapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hospitalapplication.databinding.ItemDoctorCallBinding
import com.example.hospitalapplication.models.CallsData

class AdapterRecyclerCallsDoctor : RecyclerView.Adapter<AdapterRecyclerCallsDoctor.Holder>() {
    var data : ArrayList<CallsData> ?= null
    var onUserClick : OnUserClick?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemDoctorCallBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = data!![position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    inner class Holder (private val binding: ItemDoctorCallBinding) : RecyclerView.ViewHolder(binding.root){

        init {
            binding.accept.setOnClickListener {
                onUserClick?.onAccept(data?.get(layoutPosition)?.id!!)
            }
            binding.busyBtn.setOnClickListener {
                onUserClick?.onReject(data?.get(layoutPosition)?.id!!)
            }
        }

        fun bind(callsData :CallsData){
            binding.name.text = callsData.patient_name
            binding.date.text = callsData.created_at
        }

    }

    interface OnUserClick {
        fun onAccept (id : Int)
        fun onReject (id : Int)
    }
}