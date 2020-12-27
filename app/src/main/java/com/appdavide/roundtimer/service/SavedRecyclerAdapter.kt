package com.appdavide.roundtimer.service

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appdavide.roundtimer.R
import com.appdavide.roundtimer.data.WorkoutDb.WorkoutDb
import kotlinx.android.synthetic.main.saved_list_item.view.*

class SavedRecyclerAdapter : RecyclerView.Adapter<SavedRecyclerAdapter.savedViewHolder>() {

    private var items = emptyList<WorkoutDb>()
    public lateinit var delImg : ImageView
    public lateinit var workoutToplay : WorkoutDb

    private var mListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onDeleteClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }


    inner class savedViewHolder(savedView: View) : RecyclerView.ViewHolder(savedView){
        val workoutNameText: TextView = savedView.findViewById(R.id.workoutNameTextView)

        init {
            savedView.setOnClickListener {
                // Send Intent and Extra to open another activity on savedItems press
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
        delImg = holder.itemView.img_saved_play

        delImg.setOnClickListener {
            workoutToplay = items[position]
            mListener?.onDeleteClick(position)
        }
    }

    override fun getItemId(position: Int): Long {
        return items.get(position).id
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