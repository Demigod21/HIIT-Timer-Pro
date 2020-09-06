package com.appdavide.roundtimer.service

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appdavide.roundtimer.R
import com.appdavide.roundtimer.data.WorkoutDb.WorkoutDbAndRoundsDb
import com.appdavide.roundtimer.models.Round
import kotlinx.android.synthetic.main.saved_list_item.view.*

class SavedRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: List<WorkoutDbAndRoundsDb> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder{

        return SavedViewHolder1(
            LayoutInflater.from(parent.context).inflate(R.layout.saved_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder){
            is SavedRecyclerAdapter.SavedViewHolder1 -> {
                holder.bind(items.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(workoutDbList : List<WorkoutDbAndRoundsDb>){
        items=workoutDbList
    }

    class SavedViewHolder1 constructor(
        itemView: View
    ): RecyclerView.ViewHolder(itemView){
        val workout_name = itemView.text_titolo

        fun bind(workout: WorkoutDbAndRoundsDb){
            workout_name.setText(""+workout.workoutDb.name)
        }

    }
}