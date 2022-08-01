package com.example.pollingstar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.example.pollingstar.Mainfragments.CreateFragment
import com.example.pollingstar.Mainfragments.MainFragment
import com.example.pollingstar.Mainfragments.UserPollsFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.nio.channels.AsynchronousFileChannel.open

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var drawer : DrawerLayout
    lateinit var toggle : ActionBarDrawerToggle
    lateinit var db : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference


        val curuserref = db.child("users")


        val nav = findViewById<NavigationView>(R.id.navigationview)
        val header = nav.getHeaderView(0)
        val name = header.findViewById<TextView>(R.id.name)
        val email = header.findViewById<TextView>(R.id.email)

        val userRef = db.child("users").child(mAuth.currentUser!!.uid.toString())

        userRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val n = snapshot.child("name").getValue().toString()
                val e = snapshot.child("email").getValue().toString()
                //val uid = snapshot.child("uid").getValue().toString()
                name.text = n
                email.text = e
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        val navigation = findViewById<NavigationView>(R.id.navigationview)
        val fragmentcont = findViewById<FragmentContainerView>(R.id.fragment)
        drawer = findViewById<DrawerLayout>(R.id.drawerlayout)
        toggle = ActionBarDrawerToggle(this,drawer, R.string.open, R.string.close)
        toggle.syncState()
        drawer.addDrawerListener(toggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.beginTransaction().replace(R.id.fragment, MainFragment()).commit()

        navigation.setNavigationItemSelectedListener{

            when(it.itemId) {
                R.id.create -> {
                    //Toast.makeText(applicationContext, "moving to create fragmet", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment, CreateFragment()).commit()
                    drawer.closeDrawers()
                }

                R.id.mypolls -> {
                    //Toast.makeText(applicationContext, "hello", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment, UserPollsFragment()).commit()
                    drawer.closeDrawers()
                }


                R.id.logout -> {
                    mAuth.signOut()
                    //Toast.makeText(applicationContext, "hey", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.home -> {
                    //Toast.makeText(applicationContext, "moving to create fragmet", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment, MainFragment()).commit()
                    drawer.closeDrawers()
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}