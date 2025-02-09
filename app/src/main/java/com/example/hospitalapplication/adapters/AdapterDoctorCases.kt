package com.example.hospitalapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hospitalapplication.databinding.ItemCaseBinding
import com.example.hospitalapplication.models.CasesData

class AdapterDoctorCases  : RecyclerView.Adapter<AdapterDoctorCases.Holder>() {
    var data : ArrayList<CasesData> ?= null
    var onShowClick : OnShowClick?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemCaseBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = data!![position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    inner class Holder (private val binding: ItemCaseBinding) : RecyclerView.ViewHolder(binding.root){

        init {
            binding.show.setOnClickListener {
                onShowClick?.onShow(data?.get(layoutPosition)?.id!!)
            }
        }

        fun bind(casesData : CasesData){
            binding.name.text = casesData.patient_name
            binding.date.text = casesData.created_at
        }

    }

    interface OnShowClick {
        fun onShow (id : Int)
    }
}