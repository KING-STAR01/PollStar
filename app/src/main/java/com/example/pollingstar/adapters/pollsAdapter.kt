package com.example.pollingstar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pollingstar.Question
import com.example.pollingstar.R

class pollsAdapter(val context: Context, var arr : ArrayList<Question>, val IndPoll : IndPoll) : RecyclerView.Adapter<pollsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pollsitem, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name.text = arr.get(position).question
        holder.ans.text = arr.get(position).votes.toString()
        holder.owner.text= arr.get(position).name

        holder.itemView.setOnClickListener {

            IndPoll.redirecttodisplay(arr.get(position).uid, arr.get(position))
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return arr.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById<TextView>(R.id.pollquestion)
        val ans = itemView.findViewById<TextView>(R.id.answeredby)
        val owner = itemView.findViewById<TextView>(R.id.itemscreatedby)
    }

}

interface IndPoll {
    fun redirecttodisplay(uid : String, poll : Question)

}