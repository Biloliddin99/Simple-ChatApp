package com.example.simple_chatapp.fragments

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.simple_chatapp.R
import com.example.simple_chatapp.adapters.MyMessageAdapter
import com.example.simple_chatapp.adapters.MyRvAdapter
import com.example.simple_chatapp.adapters.RvClick
import com.example.simple_chatapp.databinding.FragmentPrivateChatsBinding
import com.example.simple_chatapp.models.MyMessage
import com.example.simple_chatapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val ARG_PARAM1 = "param1"
private const val TAG = "PrivateChatsFragment"

class PrivateChatsFragment : Fragment(), RvClick {

    private var param1: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getBoolean(ARG_PARAM1)
        }
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var userReference: DatabaseReference
    private lateinit var myRvAdapter: MyRvAdapter
    private lateinit var messageReference: DatabaseReference
    private lateinit var myMessageAdapter: MyMessageAdapter
    private val binding by lazy { FragmentPrivateChatsBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {


        auth = FirebaseAuth.getInstance()
        myMessageAdapter = MyMessageAdapter(auth.uid!!)
        firebaseDatabase = FirebaseDatabase.getInstance()
        userReference = firebaseDatabase.getReference("users")
        messageReference = firebaseDatabase.getReference("my_chat")
        myRvAdapter = MyRvAdapter(rvClick = this)
        binding.myRv.adapter = myRvAdapter

            myPrivateChats()
//        if (param1 == true) {
//        } else {
//            Toast.makeText(context, "Groups", Toast.LENGTH_SHORT).show()
//        }

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: Boolean) =
            PrivateChatsFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_PARAM1, param1)
                }
            }
    }

    override fun onClick(user: User) {
        findNavController().navigate(R.id.messageFragment, bundleOf("key" to user))
    }

    private fun myPrivateChats() {
        userReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                for (child in children) {
                    val user = child.getValue(User::class.java)
                    if (user !== null) {
                        myRvAdapter.list.add(user)
                        userMessage(user)
                    }
                }
                myRvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun userMessage(user: User) {
        messageReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                myRvAdapter.list2.clear()
                val children = snapshot.children
                val list = ArrayList<MyMessage>()
                for (child in children) {
                    val message = child.getValue(MyMessage::class.java)
                    if (message != null) {
                        if ((message.fromUid == auth.uid && message.toUid == user.id) || (message.fromUid == user.id && message.toUid == auth.uid)) {
                            list.add(message)
                        }
                    }
                }
                myRvAdapter.list2.addAll(list)
                myRvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//// Remove the ValueEventListener when the fragment is destroyed
//        userMessageListener?.let {
//            messageReference.removeEventListener(it)
//        }
//    }

}