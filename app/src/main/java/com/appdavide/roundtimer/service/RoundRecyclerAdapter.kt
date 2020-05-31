package com.appdavide.roundtimer.service

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appdavide.roundtimer.R
import com.appdavide.roundtimer.models.Round
import kotlinx.android.synthetic.main.layout_round_list_item.view.*

class RoundRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: List<Round> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return RoundViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_round_list_item, parent, false)
        )

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder){
            is RoundViewHolder -> {
                holder.bind(items.get(position))
            }
        }
    }


    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(roundList : List<Round>){
        items=roundList
    }


    class RoundViewHolder constructor(
        itemView: View
    ):RecyclerView.ViewHolder(itemView){

        val round_type = itemView.txt_round_type
        val round_work = itemView.txt_round_work
        val round_rest = itemView.txt_round_rest
        val round_cycles = itemView.txt_round_cycles
        val round_duration = itemView.txt_round_duration

        fun bind(round: Round){
            round_type.setText(""+round.type)
            round_work.setText(""+round.workDur)
            round_rest.setText(""+round.restDur)
            round_cycles.setText(""+round.cycles)
            round_duration.setText(""+round.dur)

        }

    }

}