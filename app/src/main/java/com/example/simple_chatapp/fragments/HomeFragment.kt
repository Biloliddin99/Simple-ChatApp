package com.example.simple_chatapp.fragments

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.simple_chatapp.R
import com.example.simple_chatapp.adapters.MyViewPagerAdapter
import com.example.simple_chatapp.databinding.FragmentHomeBinding
import com.example.simple_chatapp.databinding.MyTabItemBinding
import com.example.simple_chatapp.fragments.adapters.MyRvAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HomeFragment : Fragment() {

    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private lateinit var myViewPagerAdapter: MyViewPagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        myViewPagerAdapter = MyViewPagerAdapter(this)
        binding.myViewPager.adapter = myViewPagerAdapter

        val list = arrayOf("Chats", "Groups")

        binding.myTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                val selectedBackground = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.tab_item_selected
                ) as Drawable
                customView?.background = selectedBackground
                customView?.findViewById<TextView>(R.id.tab_tv_item)
                    ?.setTextColor(Color.WHITE)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                val selectedBackground = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.tab_item_unselected
                ) as Drawable
                customView?.background = selectedBackground
                customView?.findViewById<TextView>(R.id.tab_tv_item)
                    ?.setTextColor(Color.parseColor("#848484"))

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        TabLayoutMediator(binding.myTabLayout, binding.myViewPager) { tab, position ->
            val tabItemView = MyTabItemBinding.inflate(layoutInflater)
            tabItemView.tabTvItem.text = list[position]
            tab.customView = tabItemView.root

        }.attach()


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.myViewPager.adapter = null
    }
}