package com.example.pollingstar.Mainfragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pollingstar.Options
import com.example.pollingstar.R
import com.example.pollingstar.User
import com.example.pollingstar.adapters.optionAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class CreateFragment : Fragment() {

    lateinit var db : DatabaseReference
    lateinit var adapter : optionAdapter
    lateinit var mAuth : FirebaseAuth
    lateinit var curuser : User

    var arr : ArrayList<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setRetainInstance(true)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create, container, false)
        (activity as AppCompatActivity).supportActionBar?.setTitle("Create Poll")

        val question = view.findViewById<EditText>(R.id.createquestion)
        val option = view.findViewById<EditText>(R.id.createoption)
        val createbtn = view.findViewById<Button>(R.id.createpoll)
        val addbtn = view.findViewById<Button>(R.id.createadd)
        val recyler = view.findViewById<RecyclerView>(R.id.createrecycler)
        //(arr as ArrayList<String>).add("hi")

        arr = ArrayList<String>()
        adapter = optionAdapter(requireContext(), arr!!)
        recyler.adapter = adapter
        recyler.layoutManager = LinearLayoutManager(requireContext())



        db = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()

        val userRef = db.child("users").child(mAuth.currentUser!!.uid.toString())

        userRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                    val name = snapshot.child("name").getValue().toString()
                    val email = snapshot.child("email").getValue().toString()
                    val uid = snapshot.child("uid").getValue().toString()

                    curuser = User(name, uid, email)
                Log.i("mytag",curuser.toString())

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


        val arr = ArrayList<String>()
        addbtn.setOnClickListener {
            val op : String = option.text.toString()
            if(op.trim()<=" "){
                Toast.makeText(requireContext(), "options must be valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            arr.add(op)
            option.setText("")
           adapter.arr = arr
            adapter.notifyDataSetChanged()
        }

        createbtn.setOnClickListener {
            val ques = question.text.toString()
            if(arr.size == 0){
                Toast.makeText(requireContext(), "options can't be empty",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(ques.trim() <= " ") {
                Toast.makeText(requireContext(), "question can't be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val pollid = db.child("polls").push().key
            db.child("polls").child(pollid.toString()).child("question").push().setValue(ques).addOnCompleteListener {
                if(it.isSuccessful) {
                    db.child("polls").child(pollid.toString()).child("pollid").setValue(pollid.toString())
                    for (i in arr) {
                        val key = db.child("polls").child(pollid.toString()).child("options").push().key
                        val option = Options(key.toString(), i, 0)
                        db.child("polls").child(pollid.toString()).child("options").child(key.toString()).setValue(option)
                        }
                }
                else {
                    Toast.makeText(requireContext(), it.exception!!.message, Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }
            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    db.child("polls").child(pollid.toString()).child("createdby").setValue(mAuth.currentUser!!.uid.toString())

                    db.child("polls").child(pollid.toString()).child("name").setValue(curuser.name)

                    arr.clear()
                    adapter.arr = arr
                    adapter.notifyDataSetChanged()
                    requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragment, MainFragment()).commit()
                }
            }

        }

        return view
    }


}


