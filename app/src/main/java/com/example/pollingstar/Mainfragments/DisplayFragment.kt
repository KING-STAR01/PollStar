package com.example.pollingstar.Mainfragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pollingstar.PollTracker
import com.example.pollingstar.Question
import com.example.pollingstar.R
import com.example.pollingstar.adapters.AnswerPoll
import com.example.pollingstar.adapters.DisplayPollAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class DisplayFragment : Fragment(), AnswerPoll {

    lateinit var db : DatabaseReference
    lateinit var mAuth : FirebaseAuth
    lateinit var adapter : DisplayPollAdapter
    lateinit var questiontext : TextView
    lateinit var retrack : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_display, container, false)

        db = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()

        val args = this.arguments

        val poll = args!!.getParcelable<Question?>("poll")!!
        //Toast.makeText(requireContext(), poll!!.name, Toast.LENGTH_SHORT).show()

        val recycler = view.findViewById<RecyclerView>(R.id.displayrecyler)
        adapter = DisplayPollAdapter(requireContext(), poll!!, this)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())
        questiontext = view.findViewById<TextView>(R.id.displayquestion)
        questiontext.text = poll.question
        retrack = view.findViewById(R.id.retrack)
        if(poll.answeredby.contains(mAuth.currentUser!!.uid)) {
            retrack.visibility = View.VISIBLE
        }

        (activity as AppCompatActivity).supportActionBar?.setTitle("Poll")

        retrack.setOnClickListener {
            val ind = poll.answeredby.indexOf(mAuth.currentUser!!.uid.toString())
            for(i in poll.options) {
                if(i.uid == poll.votechoice[ind] && i.votes > 0){
                    i.votes -= 1
                    db.child("polls").child(poll.uid).child("options").child(i.uid).setValue(i).addOnCompleteListener {
                        if(it.isSuccessful) {
                            db.child("polls").child(poll.uid).child("answeredby").child(mAuth.currentUser!!.uid).removeValue().addOnCompleteListener {
                                if(it.isSuccessful) {
                                    poll.answeredby.removeAt(ind)
                                    poll.votechoice.removeAt(ind)

                                    adapter.poll = poll
                                    adapter.notifyDataSetChanged()
                                    retrack.visibility = View.GONE
                                }
                            }

                        }
                    }


                    return@setOnClickListener



                }
            }


        }

        return view
    }

    override fun redirect(poll: Question, i : Int) {

        //poll.options[i].votes += 1
        val option = poll.options[i]
        //option.votes+= 1
        option.votes += 1

        Log.i("uid", poll.options.size.toString())
        db.child("polls").child(poll.uid).child("options").child(poll.options[i].uid.toString()).setValue(poll.options[i])
            .addOnCompleteListener {
                if(it.isSuccessful) {

                    db.child("polls").child(poll.uid).child("answeredby").child(mAuth.currentUser!!.uid).setValue(PollTracker(mAuth.currentUser!!.uid.toString(), poll.options[i].uid.toString())).addOnCompleteListener {
                        if(it.isSuccessful) {

                            Log.i("mytag", poll.options.size.toString())
                            poll.answeredby.add(mAuth.currentUser!!.uid)
                            poll.votechoice.add(poll.options[i].uid.toString())
                            Toast.makeText(requireContext(), "your vote has been casted", Toast.LENGTH_SHORT).show()
                            adapter.poll.options[i] = option
                            for(i in poll.options) {
                                Log.i("options", i.value.toString())
                            }
                            adapter.poll = poll
                            adapter.notifyDataSetChanged()
                            retrack.visibility = View.VISIBLE

                        }
                        else
                        {
                            Toast.makeText(requireContext(), "failed to caste vote try again", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }





    }

    override fun redirectunasnwered(uid: String, poll: Question) {

    }

}