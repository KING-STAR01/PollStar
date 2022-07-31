package com.example.pollingstar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.pollingstar.R

class optionAdapter(val context: Context, var arr : ArrayList<String>) : RecyclerView.Adapter<optionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.optioncard, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.option.text = arr!!.get(position).toString()

        holder.del.setOnClickListener {
            Toast.makeText(context,"hey" ,Toast.LENGTH_SHORT).show()
            arr!!.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return arr!!.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val option = itemView.findViewById<TextView>(R.id.cardname)
        val del = itemView.findViewById<ImageView>(R.id.carddelete)


    }


}