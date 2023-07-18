package com.example.simple_chatapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.simple_chatapp.R
import com.example.simple_chatapp.adapters.MyGroupMessageAdapter
import com.example.simple_chatapp.adapters.MyMessageAdapter
import com.example.simple_chatapp.databinding.FragmentGroupMessageBinding
import com.example.simple_chatapp.databinding.FragmentMessageBinding
import com.example.simple_chatapp.models.Group
import com.example.simple_chatapp.models.MyMessage
import com.example.simple_chatapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GroupMessageFragment : Fragment() {

    private val binding by lazy { FragmentGroupMessageBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var groupReference: DatabaseReference
    private lateinit var messageReference: DatabaseReference
    private lateinit var userReference: DatabaseReference
    private lateinit var myGroupMessageAdapter: MyGroupMessageAdapter
    private lateinit var group: Group
    var memberList: ArrayList<User> = ArrayList()
    lateinit var groupList: ArrayList<Group>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        group = arguments?.getSerializable("key2") as Group

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        myGroupMessageAdapter = MyGroupMessageAdapter(auth.uid!!)
        binding.myRv.adapter = myGroupMessageAdapter
        groupReference = firebaseDatabase.getReference("groups")
        messageReference = firebaseDatabase.getReference("group_chat")
        userReference = firebaseDatabase.getReference("users")
        binding.tvName.text = group.name

        writeFirebase()
        groupTable()
        userTable()
        readFireBase()


        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        return binding.root
    }


    private fun writeFirebase() {
        binding.apply {
            edtMessage.addTextChangedListener {
                if (edtMessage.text.isNotEmpty()) {
                    btnSend.visibility = View.VISIBLE
                    btnSend.setOnClickListener {
                        val id = messageReference.push().key
                        val myMessage = MyMessage(
                            id,
                            auth.currentUser?.uid,
                            group.name,
                            edtMessage.text.toString().trim()
                        )
                        messageReference.child(id!!).setValue(myMessage)
                        edtMessage.text.clear()
                    }
                } else {
                    btnSend.visibility = View.GONE
                }
            }
        }
    }

    private fun groupTable() {
        groupReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                groupList = ArrayList()
                val children = snapshot.children
                for (child in children) {
                    val value = child.getValue(Group::class.java)
                    if (value != null) {
                        groupList.add(value)
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Internet bilan bog'liq muammo", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun userTable() {
        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                memberList = ArrayList()
                val children = snapshot.children
                for (child in children) {
                    val value = child.getValue(User::class.java)
                    if (value != null) {
                        memberList.add(value)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun readFireBase() {
        messageReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                myGroupMessageAdapter.list.clear()
                val children = snapshot.children
                for (child in children) {
                    val message = child.getValue(MyMessage::class.java)
                    if (message != null && message.toUid == group.name) {
                        myGroupMessageAdapter.list.add(message)
                    }
                }

                myGroupMessageAdapter.notifyDataSetChanged()
                binding.myProgress.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                binding.myProgress.visibility = View.GONE
            }
        })
    }
}