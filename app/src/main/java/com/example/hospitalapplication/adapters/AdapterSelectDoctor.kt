package com.example.hospitalapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hospitalapplication.R
import com.example.hospitalapplication.databinding.ItemDoctorBinding
import com.example.hospitalapplication.models.EmployeeData

class AdapterSelectDoctor  : RecyclerView.Adapter<AdapterSelectDoctor.Holder>() {

    var data : ArrayList<EmployeeData> ?= null
    private lateinit var onClick : (Int) -> Unit
    var index  = -1
    var onUserClick : OnUserClick?= null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemDoctorBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = data!![position]
        holder.bind(item)
        if (index == position){
            holder.radioButton.setImageResource(R.drawable.green_radio_button)
        }else{
            holder.radioButton.setImageResource(R.drawable.gray_radio_button)
        }
    }

    inner class Holder (private val binding: ItemDoctorBinding) : RecyclerView.ViewHolder(binding.root) {
        var radioButton = binding.selection
        init {
            itemView.setOnClickListener {
                index = layoutPosition
                onUserClick?.onClick(data?.get(layoutPosition)?.id!!,data?.get(layoutPosition)?.first_name!!)
                notifyDataSetChanged()
            }
        }
        fun bind(employeeData: EmployeeData){
            binding.docName.text = employeeData.first_name
            binding.type.text = employeeData.type
        }
    }
    interface OnUserClick {
        fun onClick (id : Int , name :String)
    }
}