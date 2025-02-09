package com.example.hospitalapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hospitalapplication.R
import com.example.hospitalapplication.databinding.ItemCallsBinding
import com.example.hospitalapplication.models.CallsData

class AdapterCalls(): RecyclerView.Adapter<AdapterCalls.Holder>() {

    var data:List<CallsData> ? = null

    private lateinit var onClick : (Int) -> Unit
    fun setOnClick(onClick : (Int) -> Unit)
    {
        this.onClick = onClick
    }


    fun updateList(newItems: List<CallsData>) {
        data = newItems
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemCallsBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return data?.size?:0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = data!![position]
        holder.bind(item)
    }


    inner class Holder(private val binding: ItemCallsBinding) : RecyclerView.ViewHolder(binding.root)
    {
        init {
            binding.root.setOnClickListener{
                var idd = data?.get(layoutPosition)?.id!!
                onClick.invoke(idd)
            }
        }
        fun bind(callsData: CallsData){
            binding.name.text = callsData.patient_name
            binding.date.text = callsData.created_at
            if(callsData.status == "pending" || callsData.status == "pending_doctor"){
                binding.doneOrNot.setImageResource(R.drawable.orange_clock)
            }
            else if (callsData.status == "logout"){
                binding.doneOrNot.setImageResource(R.drawable.green_right)
            }
        }
    }
}