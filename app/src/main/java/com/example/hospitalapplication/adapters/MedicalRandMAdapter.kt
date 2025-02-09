package com.example.hospitalapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hospitalapplication.R

class MedicalRandMAdapter(): RecyclerView.Adapter<MedicalRandMAdapter.Holder>() {
    var pos = 0
    var data:ArrayList<String> ?= null
    private lateinit var onClick : (String) -> Unit
    fun setOnClick(onClick : (String) -> Unit)
    {
        this.onClick = onClick
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_hor_with_x , parent , false)
        return Holder(item)
    }

    override fun getItemCount():Int {
        return data?.size?:0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = data?.get(position)
        holder.apply {

            holder.textView.text = data
        }
    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view)
    {
        val textView = view.findViewById<TextView>(R.id.item_button)!!
        var context:Context ?= null
        init {
            context = view.context
            textView.setOnClickListener {
                pos = layoutPosition
                onClick.invoke(data!![layoutPosition])
                notifyDataSetChanged()
            }
        }
        fun bind(item: String) {
            textView.text = item
        }
    }
}