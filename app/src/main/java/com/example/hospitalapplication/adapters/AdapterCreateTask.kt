package com.example.hospitalapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hospitalapplication.R

class AdapterCreateTask : RecyclerView.Adapter<AdapterCreateTask.Holder>() {

    var list: ArrayList<String>? = null

    var onTaskDeleted  :OnTaskDeleted?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_create_task, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = list?.get(position)
        holder.apply {
            textTittle.text = data
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val textTittle = view.findViewById<TextView>(R.id.text_todo)
        val btnDeleteTask = view.findViewById<ImageView>(R.id.btn_delete_task)
        init {
            btnDeleteTask.setOnClickListener {
                onTaskDeleted?.onDelete(layoutPosition)
            }
        }
    }
    interface OnTaskDeleted{
        fun onDelete (id : Int)
    }
}