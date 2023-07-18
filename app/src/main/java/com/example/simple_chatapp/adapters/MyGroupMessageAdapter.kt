package com.example.simple_chatapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simple_chatapp.databinding.ItemFromMessageBinding
import com.example.simple_chatapp.databinding.ItemToMessageBinding
import com.example.simple_chatapp.models.MyMessage
import com.squareup.picasso.Picasso

class MyGroupMessageAdapter( val uid:String,val list: ArrayList<MyMessage> = ArrayList()) :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 1) {
            val hold = holder as FromVh
            hold.onBind(list[position])
        } else {
            val hold = holder as ToVh
            hold.onBind(list[position])
        }
    }
}