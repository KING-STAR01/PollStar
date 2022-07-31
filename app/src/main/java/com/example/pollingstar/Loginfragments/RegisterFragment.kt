package com.example.pollingstar.Loginfragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.pollingstar.MainActivity
import com.example.pollingstar.R
import com.example.pollingstar.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RegisterFragment : Fragment() {

    private var mAuth : FirebaseAuth? = null
    private var db : DatabaseReference? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        val name = view.findViewById<EditText>(R.id.regname)
        val email = view.findViewById<EditText>(R.id.regemail)
        val pass = view.findViewById<EditText>(R.id.regpass)
        val login = view.findViewById<TextView>(R.id.reglogin)
        val btn = view.findViewById<Button>(R.id.regbtn)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().getReference()

        btn.setOnClickListener {
            val n = name.text.toString()
            val e = email.text.toString()
            val p = pass.text.toString()

            if(n <= " " || e <= " " || p <= " ") {
                Toast.makeText(requireContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mAuth!!.createUserWithEmailAndPassword(e,p).addOnCompleteListener {

                if(it.isSuccessful) {
                    val user = User(n, mAuth!!.currentUser!!.uid, e)
                    db!!.child("users").child(mAuth!!.currentUser!!.uid).setValue(user)
                    Toast.makeText(requireContext(), "User Registration successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    (activity as AppCompatActivity).startActivity(intent)
                    (activity as AppCompatActivity).finish()
                }
                else {
                    Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

        login.setOnClickListener {
            requireActivity().onBackPressed()
        }
        return view
    }


}