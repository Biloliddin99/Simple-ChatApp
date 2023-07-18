package com.example.simple_chatapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.example.simple_chatapp.fragments.GroupChatsFragment
import com.example.simple_chatapp.fragments.PrivateChatsFragment


class MyViewPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {
/*
    private val privateChats = mutableListOf<User>()
    private val groupChats = mutableListOf<Group>()

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return if (position == 0)
            PrivateChatsFragment.newInstance(true)
        else GroupChatsFragment.newInstance(false)
    }
*/


    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0)
            PrivateChatsFragment.newInstance(true)
        else GroupChatsFragment.newInstance(false)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return itemId in 0 until itemCount

    }

}