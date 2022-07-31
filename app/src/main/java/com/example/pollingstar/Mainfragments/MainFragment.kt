package com.example.pollingstar.Mainfragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pollingstar.MainActivity
import com.example.pollingstar.Options
import com.example.pollingstar.Question
import com.example.pollingstar.R
import com.example.pollingstar.adapters.IndPoll
import com.example.pollingstar.adapters.pollsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class MainFragment : Fragment(), IndPoll {

    lateinit var db: DatabaseReference
    lateinit var adapter: pollsAdapter
    lateinit var ques: ArrayList<Question>
    lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val recycler = view.findViewById<RecyclerView>(R.id.mainrecyler)
        (activity as AppCompatActivity).supportActionBar?.setTitle("Poll")



        db = FirebaseDatabase.getInstance().reference

        ques = ArrayList()


        val pollsRef = db.child("polls")
        val usersRef = db.child("users")
        pollsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    ques.clear()
                    for (poll in snapshot.children) {
                        val uid = poll.child("createdby").getValue().toString()
                        //Log.i("mytag",uid.toString())
                        val options = ArrayList<Options>()
                        val answeredby = ArrayList<String>()
                        val votechoice = ArrayList<String>()
                        var votes = 0
                        val optionsRef = poll.child("options")
                        for (c in optionsRef.children) {
                            val value = c.child("value").getValue().toString()
                            val vote = c.child("votes").getValue().toString().toInt()
                            //Log.i("mytag", value)
                            val option = Options(c.key, value, vote)
                            options.add(option!!)
                            votes += vote
                        }
                        var question = ""
                        val qu = poll.child("question").children
                        for (q in qu) {
                            question = q.getValue().toString()
                        }
                        //Log.i("mytag",question)
                        for (child in poll.child("answeredby").children) {
                            val option = child.child("userid").getValue().toString()
                            answeredby.add(option)
                            val choice = child.child("pollid").getValue().toString()
                            votechoice.add(choice)
                        }
                        val name = poll.child("name").getValue().toString()
                        val pollid = poll.child("pollid").getValue().toString()
                        //Toast.makeText(requireContext(), pollid, Toast.LENGTH_SHORT).show()
                        val Poll = Question(pollid, name!!, question, votes, uid, options, answeredby, votechoice)
                        ques.add(Poll)


                    }
                    adapter.arr = ques!!

                    adapter.notifyDataSetChanged()
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        adapter = pollsAdapter(requireContext(), ques, this)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())



        return view
    }

    override fun redirecttodisplay(uid: String, polli: Question) {

        val fragment1 = DisplayFragment()
        val bundle = Bundle()
        bundle.putParcelable("poll", polli)
        fragment1.arguments = bundle
        for(i in polli.options) {
            Log.i("polli",i.value)
        }

        val tran = requireActivity().supportFragmentManager.beginTransaction()
        tran.replace(R.id.fragment, fragment1)
        //tran.addToBackStack(null)
        tran.commit()







        /*

        var poll : Question? = polli

        val optionss = ArrayList<Options>()
        val answeredby = ArrayList<String>()
        val votechoice = ArrayList<String>()
        val p = db.child("polls").child(uid)
        p.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                votechoice.clear()
                answeredby.clear()
                optionss.clear()
                val uid = snapshot.child("createdby").getValue().toString()
                //Log.i("mytag",uid.toString())
                var votes = 0
                val optionsRef = snapshot.child("options")
                for (c in optionsRef.children) {
                    val value = c.child("value").getValue().toString()
                    val vote = c.child("votes").getValue().toString().toInt()
                    //Log.i("mytag", value)
                    val option = Options(c.key,value, vote)
                    optionss.add(option!!)
                    votes += vote
                }
                var question = ""
                val qu = snapshot.child("question").children
                for(q in qu) {
                    question = q.getValue().toString()
                }

                //Log.i("mytag",question)
                for (child in snapshot.child("answeredby").children) {
                    val option = child.child("userid").getValue().toString()
                    answeredby.add(option)
                    val choice = child.child("pollid").getValue().toString()
                    votechoice.add(choice)
                }
                val name = snapshot.child("name").getValue().toString()
                val pollid = snapshot.child("pollid").getValue().toString()
                //Toast.makeText(requireContext(), pollid, Toast.LENGTH_SHORT).show()
                poll = Question(pollid,name!!,question ,votes, uid, optionss,answeredby,votechoice)

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })






        //requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragment,MainFragment()).commit()


        //requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragment, DisplayFragment())

/*

         val fragment1 = DisplayFragment()
        val bundle = Bundle()
        bundle.putParcelable("poll", Poll)
        fragment1.arguments = bundle
        val trans = requireActivity().supportFragmentManager.beginTransaction()
        trans.replace(R.id.fragment, fragment1!!)
        trans.addToBackStack(null)
        trans.commit()

 */
        val bundle = Bundle()
        bundle?.putParcelable("poll",Poll)
        val fragment = DisplayFragment()

        fragment.arguments = bundle
        val tran = requireActivity().supportFragmentManager.beginTransaction()
        tran.replace(R.id.fragment, fragment)
        tran.addToBackStack(null)
        tran.commit()

         */

    }

}