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
import com.example.simple_chatapp.databinding.FragmentMessageBinding
import com.example.simple_chatapp.adapters.MyMessageAdapter
import com.example.simple_chatapp.adapters.MyRvAdapter
import com.example.simple_chatapp.adapters.RvClick
import com.example.simple_chatapp.models.MyMessage
import com.example.simple_chatapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class MessageFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var messageReference: DatabaseReference
    private lateinit var binding: FragmentMessageBinding
    private lateinit var myMessageAdapter: MyMessageAdapter
    private lateinit var user: User

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMessageBinding.inflate(layoutInflater)

        auth = FirebaseAuth.getInstance()
        myMessageAdapter = MyMessageAdapter(auth.uid!!)
        binding.myRv.adapter = myMessageAdapter
        binding.myProgress.visibility = View.VISIBLE

        user = arguments?.getSerializable("key") as User

        Picasso.get().load(user.imageLink).into(binding.imageProfil)
        binding.tvName.text = user.name

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        firebaseDatabase = FirebaseDatabase.getInstance()
        messageReference = firebaseDatabase.getReference("my_chat")

        messageReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                myMessageAdapter.list.clear()
                val children = snapshot.children
                for (child in children) {
                    val message = child.getValue(MyMessage::class.java)
                    if (message != null) {
                        if ((message.fromUid == auth.uid && message.toUid == user.id) || (message.fromUid == user.id && message.toUid == auth.uid)) {
                            myMessageAdapter.list.add(message)

                        }
                    }
                }

                myMessageAdapter.notifyDataSetChanged()
                binding.myProgress.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                binding.myProgress.visibility = View.GONE
            }
        })

        binding.apply {
            edtMessage.addTextChangedListener {
                if (edtMessage.text.isNotEmpty()) {
                    btnSend.visibility = View.VISIBLE
                    btnSend.setOnClickListener {
                        val id = messageReference.push().key
                        val myMessage = MyMessage(
                            id,
                            auth.uid,
                            user.id,
                            edtMessage.text.toString().trim()
                        )
                        messageReference.child(id!!).setValue(myMessage)
                        edtMessage.text.clear()

                        myRv.smoothScrollToPosition(myMessageAdapter.itemCount - 1)
                        myMessageAdapter.notifyDataSetChanged();
                    }
                } else {
                    btnSend.visibility = View.GONE
                }
            }
        }

        return binding.root
    }


}