package com.example.simple_chatapp.fragments


import android.annotation.SuppressLint
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
import com.example.simple_chatapp.models.User
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var userReference: DatabaseReference
    private lateinit var myRvAdapter: MyRvAdapter
    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private lateinit var myViewPagerAdapter: MyViewPagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        auth = FirebaseAuth.getInstance()

        firebaseDatabase = FirebaseDatabase.getInstance()
        userReference = firebaseDatabase.getReference("users")
        myRvAdapter = MyRvAdapter()
        binding.myRv.adapter = myRvAdapter
        myViewPagerAdapter = MyViewPagerAdapter(this)
        binding.myViewPager.adapter = myViewPagerAdapter

//        userReference.addValueEventListener(object : ValueEventListener {
//            @SuppressLint("NotifyDataSetChanged")
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val children = snapshot.children
//                for (child in children) {
//                    val user = child.getValue(User::class.java)
//                    if (user !== null) {
//                        myRvAdapter.list.add(user)
//                    }
//                }
//                myRvAdapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {


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