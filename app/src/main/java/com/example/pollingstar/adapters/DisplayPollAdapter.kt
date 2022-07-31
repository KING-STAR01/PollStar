package com.example.pollingstar.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pollingstar.Question
import com.example.pollingstar.R
import com.google.firebase.auth.FirebaseAuth

class DisplayPollAdapter(context: Context, var poll : Question, val answerpoll : AnswerPoll) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ANSWERED = 1
    val UN_ANWSERED = 0
    lateinit var mAuth : FirebaseAuth

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType == ANSWERED) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.pollsitemanswered, parent, false)
            return Answered(view)
        }
        else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.displaypollitem, parent, false)
            return Unansered(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        mAuth = FirebaseAuth.getInstance()
        if(poll.answeredby.contains(mAuth.currentUser!!.uid)) {
            (holder as Answered).option.text = poll.options[position].value
            (holder as Answered).vvotes.text = poll.options[position].votes.toString()
            Log.i("mytag", poll.options[position].value)
        }
        else {
            (holder as Unansered).option.text = poll.options[position].value
            holder.itemView.setOnClickListener {
                answerpoll.redirect(poll, position)
                notifyDataSetChanged()
                Log.i("mytag", poll.options[position].value)
            }
        }

    }

    override fun getItemCount(): Int {
        return poll.options.size
    }

    override fun getItemViewType(position: Int): Int {
        mAuth = FirebaseAuth.getInstance()
        if(poll.answeredby.contains(mAuth.currentUser!!.uid)){
            return ANSWERED
        }
        else
            return UN_ANWSERED
        //return super.getItemViewType(position)
    }

    class Unansered(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val option = itemView.findViewById<TextView>(R.id.displayoption)

    }
    class Answered(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val option = itemView.findViewById<TextView>(R.id.answeredoption)
        val vvotes = itemView.findViewById<TextView>(R.id.answeredpolls)

    }

}

interface AnswerPoll {
    fun redirect(poll: Question, i : Int)
    fun redirectunasnwered(uid: String, poll: Question)
}