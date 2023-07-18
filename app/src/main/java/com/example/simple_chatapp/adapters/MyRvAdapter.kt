package com.example.simple_chatapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simple_chatapp.databinding.ItemRvBinding
import com.example.simple_chatapp.models.MyMessage
import com.example.simple_chatapp.models.User
import com.squareup.picasso.Picasso

class MyRvAdapter(
    val list: ArrayList<User> = ArrayList(),
    val list2: ArrayList<MyMessage> = ArrayList(),
    val rvClick: RvClick,
) : RecyclerView.Adapter<MyRvAdapter.Vh>() {

    inner class Vh(private val itemRvBinding: ItemRvBinding) :
        RecyclerView.ViewHolder(itemRvBinding.root) {

        fun onBind(user: User, myMessage: MyMessage) {
            itemRvBinding.tvName.text = user.name
            itemRvBinding.tvLastMessage.text = myMessage.text
            itemRvBinding.tvItemDate.text = myMessage.date
            Picasso.get().load(user.imageLink).into(itemRvBinding.rvImage)

            itemRvBinding.root.setOnClickListener {
                rvClick.onClick(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Vh, position: Int) {
        if (list2.size > position) {
            holder.onBind(list[position], list2[position])
        } else {
            // Handle the case when list2 does not have an item at this position
            holder.onBind(list[position], MyMessage())
        }
    }

}

interface RvClick {
    fun onClick(user: User)

}