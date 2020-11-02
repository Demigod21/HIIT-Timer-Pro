package com.appdavide.roundtimer.service

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.appdavide.roundtimer.R
import com.appdavide.roundtimer.data.WorkoutDb.WorkoutDb
import com.appdavide.roundtimer.data.WorkoutDb.WorkoutDbAndRoundsDb
import com.appdavide.roundtimer.models.Round
import kotlinx.android.synthetic.main.saved_list_item.view.*

class SavedRecyclerAdapter : RecyclerView.Adapter<SavedRecyclerAdapter.savedViewHolder>() {

    private var items = emptyList<WorkoutDb>()

    inner class savedViewHolder(savedView: View) : RecyclerView.ViewHolder(savedView){
        val workoutNameText: TextView = savedView.findViewById(R.id.workoutNameTextView)

        init {
            savedView.setOnClickListener {
                //  TODO Send Intent and Extra to open another activity on savedItems press
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : savedViewHolder{

        val layoutInflater = LayoutInflater.from(parent.context)
        val savedView = layoutInflater.inflate(R.layout.saved_list_item, parent, false)
        return savedViewHolder(savedView)
    }

    override fun onBindViewHolder(holder: savedViewHolder, position: Int) {
        val current = items[position]
        holder.workoutNameText.text = current.name
    }

    override fun getItemCount(): Int {
        return items.size
    }

    internal fun setItems(items: List<WorkoutDb>){
        this.items = items
        notifyDataSetChanged()
    }

//    fun submitList(workoutDbList : LiveData<List<WorkoutDbAndRoundsDb>>){
//        items=workoutDbList
//    }

}