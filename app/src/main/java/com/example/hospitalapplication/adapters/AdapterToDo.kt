package com.example.hospitalapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hospitalapplication.R
import com.example.hospitalapplication.models.ToDo

class AdapterToDo: RecyclerView.Adapter<AdapterToDo.Holder>() {
    var list : ArrayList<ToDo> ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_to_do, parent , false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = list?.get(position)
        holder.apply {
            textTittle.text = data?.title
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class Holder (view : View) : RecyclerView.ViewHolder(view){
        val textTittle: TextView = view.findViewById(R.id.text_tittle)
    }
}