package com.example.simple_chatapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simple_chatapp.databinding.ItemGroupRvBinding
import com.example.simple_chatapp.models.Group

class MyGroupRvAdapter(val list: ArrayList<Group> = ArrayList(),val rvGroupClick: RvGroupClick) : RecyclerView.Adapter<MyGroupRvAdapter.Vh>() {

    inner class Vh(private val itemRvBinding: ItemGroupRvBinding) :
        RecyclerView.ViewHolder(itemRvBinding.root) {

        fun onBind(group: Group) {
            itemRvBinding.tvGroupName.text = group.name

            itemRvBinding.root.setOnClickListener {
                rvGroupClick.onClick(group)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemGroupRvBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

}

interface RvGroupClick {
    fun onClick(group: Group)

}