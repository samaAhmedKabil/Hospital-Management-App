package com.example.hospitalapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hospitalapplication.R

class AdapterEmployeeHorizontalRecycler(private val data:Array<String>): RecyclerView.Adapter<AdapterEmployeeHorizontalRecycler.Holder>() {
    var pos = 0
    private lateinit var onClick : (String) -> Unit
    fun setOnClick(onClick : (String) -> Unit)
    {
        this.onClick = onClick
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_horizontal , parent , false)
        return Holder(item)
    }

    override fun getItemCount():Int {
        return data.size?:0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
       val dataNum = data[position]
        holder.bind(dataNum)
        holder.apply {
            if(pos == position){
                textView.background = ContextCompat.getDrawable(context!! , R.drawable.selected_horizontal_item)
                textView.setTextColor(ContextCompat.getColor(context!! , R.color.white))
            }
            else{
                textView.background = ContextCompat.getDrawable(context!! , R.drawable.horizontal_item)
                textView.setTextColor(ContextCompat.getColor(context!! , R.color.black))
            }
        }
    }

    inner class Holder(view:View) : RecyclerView.ViewHolder(view)
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

