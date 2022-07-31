package com.example.pollingstar.Loginfragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
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


class LoginFragment : Fragment() {

    var mAuth : FirebaseAuth? = null
    var db : DatabaseReference? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val email = view.findViewById<EditText>(R.id.loginemail)
        val pass = view.findViewById<EditText>(R.id.loginpass)
        val btn = view.findViewById<Button>(R.id.loginbtn)
        val reg = view.findViewById<TextView>(R.id.loginreg)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().getReference()

        btn.setOnClickListener {

            val e = email.text.toString()
            val p = pass.text.toString()
            if(e.trim() <= " " || p.trim() <= " ") {
                Toast.makeText(requireContext(), "Email and Password cannot be empty", Toast.LENGTH_SHORT).show()

                return@setOnClickListener
            }

            mAuth!!.signInWithEmailAndPassword(e,p).addOnCompleteListener {

                if(it.isSuccessful) {

                    (activity as AppCompatActivity).startActivity(Intent(requireContext(), MainActivity::class.java))
                    (activity as AppCompatActivity).finish()
                    Log.i("tag", mAuth!!.currentUser!!.uid)
                }
                else {
                    Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }

        }

        reg.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }

        return view
    }

}