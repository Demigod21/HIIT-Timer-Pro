package com.appdavide.roundtimer.ui.saved

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appdavide.roundtimer.R
import com.appdavide.roundtimer.data.WorkoutDb.WorkoutDb
import kotlinx.android.synthetic.main.saved_list_item.view.*

class SavedAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<SavedAdapter.SavedViewHolder>(){
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var workouts = emptyList<WorkoutDb>() // Cached copy of words


    inner class SavedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
//        val workItemView: TextView = itemView.findViewById(R.id.text_titolo)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedViewHolder {
        val itemView = inflater.inflate(R.layout.saved_list_item, parent, false)
        return SavedViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SavedViewHolder, position: Int) {
        val current = workouts[position]
//        holder.workItemView.text = current.name
    }

    override fun getItemCount() = workouts.size



}