package com.example.simple_chatapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.simple_chatapp.R
import com.example.simple_chatapp.adapters.MyGroupRvAdapter
import com.example.simple_chatapp.adapters.RvGroupClick
import com.example.simple_chatapp.databinding.FragmentGroupChatsBinding
import com.example.simple_chatapp.databinding.ItemDialogAddGroupBinding
import com.example.simple_chatapp.models.Group
import com.example.simple_chatapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val ARG_PARAM1 = "param1"

class GroupChatsFragment : Fragment(), RvGroupClick {

    private var param1: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getBoolean(ARG_PARAM1)
        }
    }

    private val binding by lazy { FragmentGroupChatsBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    private lateinit var myGroupRvAdapter: MyGroupRvAdapter
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var groupReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        groupReference = firebaseDatabase.getReference("groups")
        myGroupRvAdapter = MyGroupRvAdapter(rvGroupClick = this)
        binding.rvGroup.adapter = myGroupRvAdapter

        myGroupChats()
        addGroup()

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: Boolean) =
            GroupChatsFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_PARAM1, param1)
                }
            }
    }

    override fun onClick(group: Group) {
        findNavController().navigate(R.id.groupMessageFragment, bundleOf("key2" to group))
    }

    private fun myGroupChats() {
        groupReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                myGroupRvAdapter.list.clear()
                val children = snapshot.children
                for (child in children) {
                    val group = child.getValue(Group::class.java)
                    if (group !== null) {
                        myGroupRvAdapter.list.add(group)
                    }
                }
                myGroupRvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addGroup() {
        binding.btnAdd.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireContext()).create()
            val itemDialogAddGroupBinding = ItemDialogAddGroupBinding.inflate(layoutInflater)
            alertDialog.setView(itemDialogAddGroupBinding.root)
            itemDialogAddGroupBinding.btnSave.setOnClickListener {
                val name = itemDialogAddGroupBinding.edtNameGroup.text.toString().trim()
                if (name != "") {
                    val key = groupReference.push().key
                    groupReference.child(key!!).setValue(Group(name))
                    Toast.makeText(context, "Group added", Toast.LENGTH_SHORT).show()
                    alertDialog.cancel()
                } else {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
            }
            alertDialog.show()
        }
    }

}