package com.example.pollingstar.Loginfragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.pollingstar.MainActivity
import com.example.pollingstar.R
import com.google.firebase.auth.FirebaseAuth


class SplashFragment : Fragment() {

    private var mAuth : FirebaseAuth ? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_splash, container, false)
        mAuth = FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({
            if(mAuth?.currentUser != null) {
                (activity as AppCompatActivity).startActivity(Intent(requireContext(), MainActivity::class.java))
                (activity as AppCompatActivity).finish()
            }
            else {
                val action = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
                findNavController().navigate(action)
            }

            }, 0)







        return view
    }


}

