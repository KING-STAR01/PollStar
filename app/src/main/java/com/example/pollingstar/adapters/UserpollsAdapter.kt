package com.example.pollingstar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pollingstar.Question
import com.example.pollingstar.R

class UserpollsAdapter(val context: Context, var arr : ArrayList<Question>, val userPoll : UserPoll) : RecyclerView.Adapter<UserpollsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.userpollsitem, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name.text = arr[position].question
        holder.ans.text = arr[position].votes.toString()
        holder.owner.text= arr[position].name

        holder.del.setOnClickListener {
            userPoll.delete(arr[position].uid, position)
        }
            holder.name.setOnClickListener {
                userPoll.redirect(arr[position])
            }


    }

    override fun getItemCount(): Int {
        return arr.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById<TextView>(R.id.userpollquestion)
        val ans = itemView.findViewById<TextView>(R.id.useransweredby)
        val owner = itemView.findViewById<TextView>(R.id.useritemscreatedby)
        val del = itemView.findViewById<ImageView>(R.id.userpolldelete)
    }

}

interface UserPoll {
    fun redirect(poll : Question)
    fun delete(uid : String, ind : Int)
}