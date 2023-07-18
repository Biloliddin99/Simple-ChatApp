package com.example.simple_chatapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.simple_chatapp.databinding.ItemFromMessageBinding
import com.example.simple_chatapp.databinding.ItemToMessageBinding
import com.example.simple_chatapp.models.MyMessage

class MyMessageAdapter(val uid: String, val list: ArrayList<MyMessage> = ArrayList()) :
    RecyclerView.Adapter<ViewHolder>() {

    inner class FromVh(val itemRv: ItemFromMessageBinding) : RecyclerView.ViewHolder(itemRv.root) {
        fun onBind(myMessage: MyMessage) {
            itemRv.tvName.text = myMessage.text
            itemRv.tvDate.text = myMessage.date
        }
    }

    inner class ToVh(val itemRv: ItemToMessageBinding) : RecyclerView.ViewHolder(itemRv.root) {
        fun onBind(myMessage: MyMessage) {
            itemRv.tvName.text = myMessage.text
            itemRv.tvDate.text = myMessage.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == 1) {
            FromVh(
                ItemFromMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            ToVh(ItemToMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].fromUid == uid) {
            1
        } else {
            0
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItemViewType(position) == 1) {
            val hold = holder as FromVh
            hold.onBind(list[position])
        } else {
            val hold = holder as ToVh
            hold.onBind(list[position])
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addMessage(message: MyMessage) {
        list.add(message)
        notifyDataSetChanged()
    }

}