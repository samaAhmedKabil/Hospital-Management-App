package com.example.hospitalapplication.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hospitalapplication.databinding.ItemDoctorBinding
import com.example.hospitalapplication.models.EmployeeData

class AdapterEmployeeVerticalRecycler(): RecyclerView.Adapter<AdapterEmployeeVerticalRecycler.Holder>() {
    var data:List<EmployeeData> ? = null
    private lateinit var onClick : (Int) -> Unit
    fun setOnClick(onClick : (Int) -> Unit)
    {
        this.onClick = onClick
    }

    fun updateList(newItems: List<EmployeeData>) {
        data = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemDoctorBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return data?.size?:0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = data!![position]
        holder.bind(item)
    }
    inner class Holder(private val binding: ItemDoctorBinding) : RecyclerView.ViewHolder(binding.root)
    {
        init {
            binding.root.setOnClickListener{
                var idd = data?.get(layoutPosition)?.id!!
                onClick.invoke(idd)
            }
        }
        fun bind(employeeData: EmployeeData){
            binding.docName.text = employeeData.first_name
            binding.type.text = employeeData.type
        }
    }
}